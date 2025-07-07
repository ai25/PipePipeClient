package org.schabi.newpipe.settings.importing

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.schabi.newpipe.R

class ImportCategoriesDialog : DialogFragment() {
    private lateinit var counts: Map<ImportCategory, Int>
    private lateinit var listener: (Set<ImportCategory>) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val categories = ImportCategory.values()
        val checked = BooleanArray(categories.size) { true }
        val labels = Array(categories.size) { i ->
            val category = categories[i]
            val base = requireContext().getString(category.titleRes)
            if (category == ImportCategory.SETTINGS) {
                base
            } else {
                val c = counts[category] ?: 0
                "$base (" + c + ")"
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.import_select_categories_title)
            .setMultiChoiceItems(labels, checked) { _, which, isChecked ->
                checked[which] = isChecked
            }
            .setPositiveButton(R.string.ok) { _, _ ->
                val result = categories.filterIndexed { index, _ -> checked[index] }.toSet()
                listener.invoke(result)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    companion object {
        @JvmStatic
        fun show(
            fragment: Fragment,
            counts: Map<ImportCategory, Int>,
            listener: (Set<ImportCategory>) -> Unit
        ) {
            val dialog = ImportCategoriesDialog()
            dialog.counts = counts
            dialog.listener = listener
            dialog.show(fragment.parentFragmentManager, "ImportCategoriesDialog")
        }
    }
}
