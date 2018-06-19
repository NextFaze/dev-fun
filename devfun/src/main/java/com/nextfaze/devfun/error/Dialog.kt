package com.nextfaze.devfun.error

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.overlay.OverlayManager
import kotlinx.android.synthetic.main.df_devfun_error_dialog_fragment.*
import java.lang.Math.abs
import java.text.SimpleDateFormat

internal interface RenderedError : Parcelable {
    val nanoTime: Long
    val timeMs: Long
    val stackTrace: String
    val title: CharSequence
    val body: CharSequence
    val method: CharSequence?
    var seen: Boolean
}

internal class ErrorDialogFragment : BaseDialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, errors: ArrayList<RenderedError>) =
            showNow(activity) {
                ErrorDialogFragment().apply {
                    arguments = Bundle().apply { putParcelableArrayList(ERRORS, errors) }
                }
            }
    }

    private val log = logger()
    private val handler = Handler()

    private val errorHandler by lazy { devFun.get<ErrorHandler>() }
    private val overlays by lazy { devFun.get<OverlayManager>() }

    private lateinit var errors: List<RenderedError>
    private var currentErrorIdx: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errors = arguments!!.getParcelableArrayList<RenderedError>(ERRORS).apply { sortBy { it.nanoTime } }
        currentErrorIdx = errors.indexOfFirst { !it.seen }.takeIf { it >= 0 } ?: errors.size - 1
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply { requestWindowFeature(Window.FEATURE_NO_TITLE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.df_devfun_error_dialog_fragment, container, false)

    private fun showNextError() {
        if (currentErrorIdx < errors.size - 1) {
            currentErrorIdx++
            bindCurrentError()
        }
    }

    private fun bindCurrentError() {
        setCurrentError(errors[abs(currentErrorIdx % errors.size)])
    }

    private fun showPreviousError() {
        if (currentErrorIdx > 0) {
            currentErrorIdx--
            bindCurrentError()
        }
    }

    private fun setCurrentError(error: RenderedError) {
        with(error) {
            titleTextView.text = title
            timeTextView.text = SimpleDateFormat.getDateTimeInstance().format(timeMs)
            bodyTextView.text = body
            methodTextView.text = method
            methodTextView.visibility = if (method.isNullOrBlank()) View.GONE else View.VISIBLE
            stackTraceTextView.text = stackTrace
        }
        prevButton.isEnabled = currentErrorIdx > 0
        nextButton.isEnabled = currentErrorIdx < errors.size - 1
        errorPosTextView.text = getString(R.string.df_devfun_error_position, currentErrorIdx + 1, errors.size)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stackTraceTextView.setOnLongClickListener {
            log.w { "Exception:\n${stackTraceTextView.text}" }
            true
        }

        bindCurrentError()

        prevButton.setOnClickListener { showPreviousError() }
        nextButton.setOnClickListener { showNextError() }
        navButtons?.visibility = if (errors.size > 1) View.VISIBLE else View.GONE

        clearButton.text = getString(if (errors.size > 1) R.string.df_devfun_clear_all else R.string.df_devfun_clear)
        clearButton.setOnClickListener {
            errors.forEach {
                errorHandler.remove(it.nanoTime)
            }
            dismiss()
        }

        okButton.setOnClickListener { dismiss() }

        // love me some nested 2-dimensional scrolling in dialog with constraint layout...
        errorRootConstraintLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            fixLayout()
        }
    }

    override fun onStart() {
        super.onStart()
        overlays.notifyUsingFullScreen(this)
    }

    override fun onPerformDismiss() {
        errors.forEach {
            errorHandler.markSeen(it.nanoTime)
        }
        overlays.notifyFinishUsingFullScreen(this)
    }

    private fun fixLayout() {
        if (!isResumed) return
        val rootParent = (errorRootConstraintLayout?.parent as? ViewGroup) ?: return
        log.t {
            """Current Sizes:
                    Current root.parent view size: ${rootParent.width}, ${rootParent.height}
                    Current root view size: ${errorRootConstraintLayout.width}, ${errorRootConstraintLayout.height} (minWidth=${errorRootConstraintLayout.minWidth})
                    Current root view layout params: ${errorRootConstraintLayout.layoutParams.width}, ${errorRootConstraintLayout.layoutParams.height}
                    Current titleTextView size: ${titleTextView.width}, ${titleTextView.height}
                    Current bodyTextView size: ${bodyTextView.width}, ${bodyTextView.height}
                    Current stack trace size: ${stackTraceScrollView.width}, ${stackTraceScrollView.height}
                    """.trimIndent()
        }

        fun calculateTargetSize() =
            Math.min(stackTraceScrollView.width, stackTraceTextView.width) to
                    Math.min(stackTraceScrollView.height, stackTraceTextView.height)

        val dialogIsMatchParent = dialog?.window?.attributes?.takeIf { it.width == MATCH_PARENT && it.height == MATCH_PARENT } != null
        val dialogIsWrapContent = dialog?.window?.attributes?.takeIf { it.width == WRAP_CONTENT && it.height == WRAP_CONTENT } != null

        val constraintsUpdated = (stackTraceScrollView.layoutParams as ConstraintLayout.LayoutParams).let {
            it.matchConstraintMinWidth != 0 && it.matchConstraintMinHeight != 0
        }
        val viewSizesMatched = run matched@{
            val (targetWidth, targetHeight) = calculateTargetSize()
            log.t { "targetWidth=$targetWidth, targetHeight=$targetHeight" }
            return@matched targetWidth > 0 && targetHeight > 0 &&
                    stackTraceScrollView.width == targetWidth && stackTraceScrollView.height == targetHeight
        }

        if (!constraintsUpdated || !viewSizesMatched) {
            if (!dialogIsMatchParent) {
                "Set dialog/constraint to match parent...".toastIt()
                handler.doPost {
                    dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
                    errorRootConstraintLayout?.layoutParams
                        ?.takeIf { it.width != MATCH_PARENT || it.height != MATCH_PARENT }
                        ?.apply {
                            width = MATCH_PARENT
                            height = MATCH_PARENT
                        }
                        ?.let { errorRootConstraintLayout.layoutParams = it }
                }
            } else if (!constraintsUpdated) {
                "Update constraints...".toastIt()
                handler.doPost {
                    if (errorRootConstraintLayout == null) return@doPost
                    val (targetWidth, targetHeight) = calculateTargetSize()
                    if (targetWidth > 0 && targetHeight > 0) {
                        log.t { "Update stackTraceScrollView min width/height to ($targetWidth, $targetHeight)" }
                        val constraints = ConstraintSet().apply {
                            clone(errorRootConstraintLayout)
                            constrainWidth(R.id.stackTraceScrollView, targetWidth)
                            constrainMinWidth(R.id.stackTraceScrollView, targetWidth)
                            constrainHeight(R.id.stackTraceScrollView, targetHeight)
                            constrainMinHeight(R.id.stackTraceScrollView, targetHeight)
                        }

                        errorRootConstraintLayout.setConstraintSet(constraints)
                        stackTraceScrollView.requestLayout()
                        errorRootConstraintLayout.layoutParams = errorRootConstraintLayout.layoutParams.apply {
                            width = targetWidth

                        }
                    }
                }
            } else {
                "Wrap constraint layout...".toastIt()
                handler.doPost {
                    errorRootConstraintLayout?.layoutParams
                        ?.takeIf { it.width != WRAP_CONTENT || it.height != WRAP_CONTENT }
                        ?.apply {
                            width = WRAP_CONTENT
                            height = WRAP_CONTENT
                        }
                        ?.let { errorRootConstraintLayout.layoutParams = it }
                }
            }
        } else if (!dialogIsWrapContent) {
            "Wrap dialog window...".toastIt()
            handler.doPost {
                dialog?.window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
            }
        }
    }

    private fun Handler.doPost(body: () -> Unit) {
        post(body)
//        postDelayed(body, 5000)
    }

    private fun String.toastIt() {
        log.t { "Step: $this" }
//        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

private const val ERRORS = "ERRORS"

/** Our Horizontal-Vertical ScrollView. */
internal class HVScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ScrollView(context, attrs, defStyleAttr) {

    private lateinit var hScrollView: HorizontalScrollView

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount <= 0) return
        if (childCount > 1) throw InflateException("HVScrollView can only have a single child!")

        val v = getChildAt(0)
        removeViewAt(0)

        hScrollView = HorizontalScrollView(context)
        addView(hScrollView)
        hScrollView.addView(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val superHandled = super.onTouchEvent(event)
        val hHandled = hScrollView.onTouchEvent(event)
        return superHandled || hHandled
    }
}
