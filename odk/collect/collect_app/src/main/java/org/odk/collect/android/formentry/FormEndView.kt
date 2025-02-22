package org.odk.collect.android.formentry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import org.odk.collect.android.R
import org.odk.collect.android.databinding.FormEntryEndBinding

class FormEndView(
    context: Context,
    formTitle: String,
    formEndViewModel: FormEndViewModel,
    private val listener: Listener
) : SwipeHandler.View(context) {

    private val binding = FormEntryEndBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.description.text = context.getString(R.string.save_enter_data_description, formTitle)

        binding.saveAsDraft.isVisible = formEndViewModel.isSaveDraftEnabled()
        binding.saveAsDraft.setOnClickListener {
            listener.onSaveClicked(false)
        }

        binding.finalize.isVisible = formEndViewModel.isFinalizeEnabled()
        binding.finalize.setOnClickListener {
            listener.onSaveClicked(true)
        }

        val shouldFormBeSentAutomatically = formEndViewModel.shouldFormBeSentAutomatically()
        if (formEndViewModel.hasCustomFinalizeButtonText()) {
            binding.finalize.text = formEndViewModel.customFinalizeButtonText()
        }
        else if (shouldFormBeSentAutomatically) {
            binding.finalize.text = context.getString(R.string.send)
        }

        if (!binding.saveAsDraft.isVisible && !shouldFormBeSentAutomatically) {
            binding.formEditsWarningMessage.setText(R.string.form_edits_warning_only_finalize_enabled)
        } else if (binding.saveAsDraft.isVisible && binding.finalize.isVisible) {
            if (shouldFormBeSentAutomatically) {
                binding.formEditsWarningMessage.setText(R.string.form_edits_warning_save_as_draft_and_finalize_with_auto_send_enabled)
            } else {
                binding.formEditsWarningMessage.setText(R.string.form_edits_warning_save_as_draft_and_finalize_enabled)
            }
        } else {
            binding.formEditsWarning.visibility = View.GONE
        }
    }

    override fun shouldSuppressFlingGesture() = false

    override fun verticalScrollView(): NestedScrollView? {
        return findViewById(R.id.scroll_view)
    }

    interface Listener {
        fun onSaveClicked(markAsFinalized: Boolean)
    }
}
