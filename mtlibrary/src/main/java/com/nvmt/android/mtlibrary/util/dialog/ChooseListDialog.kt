package com.nvmt.android.mtlibrary.util.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

class ChooseListDialog<T> private constructor(
    val title: String?,
    val positiveString: String?,
    val negativeString: String?,
    val checkedItem: T?,
    val itemList: List<T>?,
    val iOnItemChoose: IOnItemChoose<T>?
) {

    private constructor(builder: Builder<T>) : this(
        builder.title,
        builder.positiveString,
        builder.negativeString,
        builder.checkedItem,
        builder.itemList,
        builder.iOnItemChoose
    )

    fun show(context: Context) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        // add a radio button list
//        val itemTitleList = itemNameList?.toTypedArray()
        var check = getCheckPositionFromCheckItem(checkedItem, itemList)

        builder.setSingleChoiceItems(
            getTitleListFromItemList(itemList),
            check) { dialog, which ->
            check = which
        }

        // add OK and Cancel buttons
        builder.setPositiveButton(positiveString) { dialog, which ->
            if (check < 0) iOnItemChoose?.onItemChoose(null)
            else iOnItemChoose?.onItemChoose(itemList?.get(check))
        }
        builder.setNegativeButton(negativeString, null)

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun getTitleListFromItemList(itemList: List<T>?) : Array<CharSequence> {
        val itemTitleList = mutableListOf<String>()
        itemList?.forEach { t ->
            itemTitleList.add(iOnItemChoose!!.getTitle(t))
        }
        return itemTitleList.toTypedArray()
    }

    private fun getCheckPositionFromCheckItem(item: T?, itemList: List<T>?) : Int {
        var check = -1
        run loop@{
            itemList?.forEachIndexed { index, itemLoop ->
                if (iOnItemChoose?.getTitle(item) == iOnItemChoose?.getTitle(itemLoop)) {
                    check = index
                    return@loop
                }
            }
        }
        return check
    }


    data class Builder<T>(
        var title: String? = null,
        var positiveString: String? = null,
        var negativeString: String? = null,
        var checkedItem: T? = null,
        var itemList: List<T>? = null,
        var iOnItemChoose: IOnItemChoose<T>? = null
    ) {
        fun title(title: String) = apply {
            this.title = title
        }

        fun positiveString(positiveString: String) = apply { this.positiveString = positiveString }

        fun negativeString(negativeString: String) = apply { this.negativeString = negativeString }

        fun checkedItem(checkedItem: T?) = apply { this.checkedItem = checkedItem }

        fun itemList(itemList: List<T>) = apply { this.itemList = itemList }

        fun iOnItemChoose(iOnItemChoose: IOnItemChoose<T>) =
            apply { this.iOnItemChoose = iOnItemChoose }

        fun build() = ChooseListDialog(this)
    }

    interface IOnItemChoose<T> {
        fun onItemChoose(t: T?)

        fun getTitle(t: T?) : String
    }
}