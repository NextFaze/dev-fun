package com.nextfaze.devfun.menu

import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nextfaze.devfun.category.CategoryItem
import com.nextfaze.devfun.core.call
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.DebugException
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.internal.WithSubGroup
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.exception.ExceptionFunctionItem
import com.nextfaze.devfun.internal.splitSimpleName
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.view.ViewFactory
import kotlinx.android.synthetic.main.df_menu_dialog_fragment.*

internal class DeveloperMenuDialogFragment : BaseDialogFragment() {
    companion object {
        fun show(activity: FragmentActivity) = showNow(activity) { DeveloperMenuDialogFragment() }
        fun hide(activity: FragmentActivity) = dismiss<DeveloperMenuDialogFragment>(activity)
    }

    private val handler = Handler(getMainLooper())

    private val devMenu by lazy { devFun.devMenu }
    private val categories by lazy { devFun.categories }
    private var categoryItems = emptyList<WithText>()
    private var selectedCategoryIdx = -1

    @get:DrawableRes
    private val selectableItemBackground by lazy {
        val typedArray = dialog.context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        val resourceId = typedArray.getResourceId(0, R.drawable.df_menu_item_background)
        typedArray.recycle()
        return@lazy resourceId
    }

    private interface WithText {
        val text: CharSequence
    }

    private data class FunctionListItem(val functionItem: FunctionItem, val indent: CharSequence? = null) : WithText {
        override val text = indent?.let {
            SpannableStringBuilder().apply {
                this += indent
                this += functionItem.name
            }
        } ?: functionItem.name
    }

    private data class GroupHeaderItem(override val text: CharSequence) : WithText
    private data class SubGroupHeaderItem(override val text: CharSequence) : WithText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.df_menu_dialog_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // header
        devFun.viewFactories[MenuHeader::class]!!.inflate(layoutInflater, headerLayout).let {
            headerLayout.addView(it)
        }

        // categories
        categoriesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onBindViewHolder(holder: ViewHolder, position: Int) =
                    with(holder.textView) {
                        val category = categories[position]
                        text = category.name
                        setOnClickListener { onCategoryClick(category, position) }
                        isSelected = selectedCategoryIdx == position
                        typeface = if (isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                        lollipop {
                            when {
                                isSelected -> setBackgroundResource(R.drawable.df_menu_item_background)
                                else -> setBackgroundResource(selectableItemBackground)
                            }
                        }
                        isClickable = !isSelected
                    }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                    (from(parent.context).inflate(R.layout.df_menu_item, parent, false) as TextView).let(::ViewHolder)

                override fun getItemCount() = categories.size
            }
        }

        categoryItemsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

            class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    val item = categoryItems[position]
                    holder.textView.text = item.text
                    when (item) {
                        is FunctionListItem -> {
                            holder.textView.setOnClickListener { onCategoryItemClick(item.functionItem) }
                        }
                        is GroupHeaderItem -> {
                            holder.textView.setOnClickListener(null)
                            holder.textView.isClickable = false
                        }
                        is SubGroupHeaderItem -> {
                            holder.textView.setOnClickListener(null)
                            holder.textView.isClickable = false
                        }
                    }
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                    when (viewType) {
                        1 -> (from(parent.context).inflate(R.layout.df_menu_item, parent, false) as TextView).let(::ViewHolder)
                        2 -> (from(parent.context).inflate(R.layout.df_menu_item_sub_group, parent, false) as TextView).let(::ViewHolder)
                        else -> (from(parent.context).inflate(R.layout.df_menu_item_group, parent, false) as TextView).let(::ViewHolder)
                    }

                override fun getItemViewType(position: Int) =
                    when (categoryItems[position]) {
                        is FunctionListItem -> 1
                        is SubGroupHeaderItem -> 2
                        else -> 0
                    }

                override fun getItemCount() = categoryItems.size
            }
        }

        versionTextView.apply {
            text = getString(R.string.df_menu_version, BuildConfig.VERSION_NAME)
        }

        handler.post { categories.firstOrNull()?.let { onCategoryClick(it, 0) } }
    }

    private fun onCategoryClick(category: CategoryItem, index: Int) {
        try {
            performOnCategoryClick(category, index)
        } catch (t: Throwable) {
            devFun.get<ErrorHandler>().onError(t, "Category Generation", "Failed to generate category '${category.name}' items.")
            categoryItems = listOf(FunctionListItem(ExceptionFunctionItem(t.toString())))
            categoryItemsRecyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    private fun performOnCategoryClick(category: CategoryItem, index: Int) {
        if (selectedCategoryIdx == index) return

        val prevSelected = selectedCategoryIdx
        selectedCategoryIdx = index

        categoriesRecyclerView.adapter!!.notifyItemChanged(index)
        if (prevSelected >= 0) {
            categoriesRecyclerView.adapter!!.notifyItemChanged(prevSelected)
        }

        categoryItems = run generateCategoryItems@{
            // if no groups, then just sort and return
            if (category.items.all { it.group.isNullOrBlank() }) {
                return@generateCategoryItems category.items.map { FunctionListItem(it) }.sortedBy { it.text.toString() }
            }

            // create item group headers
            val groups = category.items.groupBy { it.group }
                .mapKeys { GroupHeaderItem(it.key ?: "Misc") }
                .toSortedMap(compareBy { it.text.toString() })

            ArrayList<WithText>().apply {
                groups.forEach {
                    add(it.key)
                    if (it.value.any { it is WithSubGroup }) {
                        it.value.groupBy { if (it is WithSubGroup) it.subGroup else null }
                            .mapKeys { it.key?.let { SubGroupHeaderItem(it) } }
                            .toSortedMap(compareBy { it?.text?.toString() })
                            .forEach {
                                val indent = it.key?.let {
                                    add(it)
                                    "\t"
                                }
                                addAll(it.value.map { FunctionListItem(it, indent) }.sortedBy { it.text.toString() })
                            }
                    } else {
                        addAll(it.value.map { FunctionListItem(it) }.sortedBy { it.text.toString() })
                    }
                }
            }
        }
        categoryItemsRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun onCategoryItemClick(functionItem: FunctionItem) {
        functionItemClicked = functionItem
        dismiss()
    }

    private var functionItemClicked: FunctionItem? = null

    override fun onPerformDismiss() {
        functionItemClicked?.let { handler.post { it.call() } }
        devMenu.onDismissed()
    }

    override fun onStart() {
        super.onStart()
        devMenu.onShown()
    }

    override fun onResume() {
        super.onResume()
        updateWindowLayoutParams()

        // When there is only a single item it is almost certainly because something went wrong
        // so we hide the categories for a bit more screen space.
        val singleItem = categories.size == 1 && categories.firstOrNull()?.items?.size == 1
        categoriesRecyclerView.visibility = if (singleItem) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        categoriesRecyclerView.adapter = null
        categoryItemsRecyclerView.adapter = null
        super.onDestroyView()
    }

    /** Update dialog window width/height (needs to be done in onResume) as it doesn't use the XML values. */
    private fun updateWindowLayoutParams() {
        view?.layoutParams?.also {
            dialog?.window?.setLayout(it.width, it.height)
            dialog?.window?.setBackgroundDrawableResource(R.drawable.df_menu_dialog_background)
        }
    }
}

private inline fun lollipop(body: () -> Any) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        body.invoke()
    }
}

internal class DefaultMenuHeaderViewFactory : ViewFactory<View> {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): View =
        inflater.inflate(R.layout.df_menu_dialog_header_layout, container, false).apply {
            // title
            findViewById<TextView>(R.id.activityTitleTextView).text = devFun.get<Activity>()::class.splitSimpleName

            // crash button
            findViewById<View>(R.id.crashButton).setOnClickListener { throw DebugException() }
        }
}
