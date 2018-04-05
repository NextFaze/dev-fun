package com.nextfaze.devfun.error

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import com.nextfaze.devfun.BaseDialogFragment
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.core.stackTraceAsString
import com.nextfaze.devfun.internal.logger
import com.nextfaze.devfun.internal.t
import com.nextfaze.devfun.obtain
import com.nextfaze.devfun.show
import kotlinx.android.synthetic.main.df_devfun_error_dialog_fragment.*

internal class ErrorDialogFragment : BaseDialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, t: Throwable, title: CharSequence, body: CharSequence, functionItem: FunctionItem? = null) =
            activity.obtain {
                ErrorDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(STACK_TRACE, t.stackTraceAsString)
                        putCharSequence(TITLE, title)
                        putCharSequence(BODY, body)
                        putCharSequence(METHOD, functionItem?.function?.method?.toString())
                    }
                }
            }.show(activity.supportFragmentManager)
    }

    private val log = logger()
    private val handler = Handler()
    private val stackTrace: String by lazy { arguments!!.getString(STACK_TRACE) }
    private val title: CharSequence by lazy { arguments!!.getCharSequence(TITLE) }
    private val body: CharSequence by lazy { arguments!!.getCharSequence(BODY) }
    private val method: CharSequence? by lazy { arguments!!.getCharSequence(METHOD) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.df_devfun_error_dialog_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleTextView.text = title
        bodyTextView.text = body
        methodTextView.text = method
        methodTextView.visibility = if (method.isNullOrBlank()) View.GONE else View.VISIBLE
        stackTraceTextView.text = stackTrace
        okButton.setOnClickListener { dismiss() }

        // love me some nested 2-dimensional scrolling in dialog with constraint layout...
        errorRootConstraintLayout.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val rootParent = (errorRootConstraintLayout.parent as ViewGroup)
            log.t {
                """Current Sizes:
                    Current root.parent view size: ${rootParent.width}, ${rootParent.height}
                    Current root view size: ${errorRootConstraintLayout.width}, ${errorRootConstraintLayout.height} (minWidth=${errorRootConstraintLayout.minWidth})
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

private const val STACK_TRACE = "STACK_TRACE"
private const val TITLE = "TITLE"
private const val BODY = "BODY"
private const val METHOD = "METHOD"

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
