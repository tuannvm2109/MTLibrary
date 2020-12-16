package com.nvmt.android.mtlibrary.util.dialog

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MultiChoiceDialog<T> private constructor(
    val title: String,
    val positiveString: String,
    val negativeString: String,
    val checkedItem: List<T>,
    val maxItemChecked: Int,
    val minItemChecked: Int,
    val itemList: List<T>,
    val iOnItemChoose: IOnItemChoose<T>?
) {
    private var selected = getBooleanArrayCheckedList(checkedItem, itemList)

    private constructor(builder: Builder<T>) : this(
        title = builder.title,
        positiveString = builder.positiveString,
        negativeString = builder.negativeString,
        checkedItem = builder.checkedItem,
        maxItemChecked = builder.maxItemChecked,
        minItemChecked = builder.minItemChecked,
        itemList = builder.itemList,
        iOnItemChoose = builder.iOnItemChoose
    )

    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        builder.setMultiChoiceItems(
            getTitleListFromItemList(itemList),
            selected
        ) { dialogInterface, position, isCheck ->
            var count = 0
            if (isCheck) {
                for (element in selected) {
                    if (element) count++
                    if (count > maxItemChecked) {
                        Toast.makeText(
                            context,
                            "Chỉ được chọn tối đa $maxItemChecked",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        selected[position] = false;
                        (dialogInterface as AlertDialog).listView.setItemChecked(position, false)
                        break
                    }
                }
            } else {
                for (element in selected) {
                    if (element) count++
                }
                if (count <= minItemChecked - 1) {
                    Toast.makeText(
                        context,
                        "Chỉ được chọn tối thiểu $minItemChecked",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    selected[position] = true;
                    (dialogInterface as AlertDialog).listView.setItemChecked(position, true)
                }
            }
        }

        builder.setPositiveButton(positiveString) { dialog, which ->
            val listChecked = mutableListOf<T>()
            selected.forEachIndexed { index, isCheck ->
                if (isCheck) listChecked.add(itemList[index])
            }
            iOnItemChoose?.onMultiItemChoosed(listChecked)
        }
        builder.setNegativeButton(negativeString, null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun getTitleListFromItemList(itemList: List<T>?): Array<CharSequence> {
        val itemTitleList = mutableListOf<String>()
        itemList?.forEach { t ->
            itemTitleList.add(iOnItemChoose!!.itemToLabel(t))
        }
        return itemTitleList.toTypedArray()
    }

    private fun getBooleanArrayCheckedList(checkedItem: List<T>?, itemList: List<T>): BooleanArray {
        val checkList = BooleanArray(itemList.size)
        val itemListLabel = itemList.map { iOnItemChoose?.itemToLabel(it) }
        val checkListLabel = checkedItem?.map { iOnItemChoose?.itemToLabel(it) } ?: return checkList

        run loop@{
            itemListLabel.forEachIndexed { index, itemLoop ->
                checkList[index] = checkListLabel.contains(itemLoop)
            }
        }
        return checkList
    }


    data class Builder<T>(
        var title: String = "Multi Choice",
        var positiveString: String = "Ok",
        var negativeString: String = "Cancel",
        var checkedItem: List<T> = ArrayList(),
        var maxItemChecked: Int = -1,
        var minItemChecked: Int = -1,
        var itemList: List<T> = ArrayList(),
        var iOnItemChoose: IOnItemChoose<T>? = null
    ) {
        fun title(title: String) = apply {
            this.title = title
        }

        fun positiveString(positiveString: String) = apply { this.positiveString = positiveString }

        fun negativeString(negativeString: String) = apply { this.negativeString = negativeString }

        fun checkedItem(checkedItem: List<T>) = apply { this.checkedItem = checkedItem }

        fun maxItemChecked(numb: Int) = apply { this.maxItemChecked = numb }

        fun minItemChecked(numb: Int) = apply { this.minItemChecked = numb }

        fun itemList(itemList: List<T>) = apply { this.itemList = itemList }

        fun iOnItemChoose(iOnItemChoose: IOnItemChoose<T>) =
            apply { this.iOnItemChoose = iOnItemChoose }

        fun build() = MultiChoiceDialog(this)
    }

    interface IOnItemChoose<T> {
        fun onMultiItemChoosed(t: List<T>)

        fun itemToLabel(t: T?): String
    }
}