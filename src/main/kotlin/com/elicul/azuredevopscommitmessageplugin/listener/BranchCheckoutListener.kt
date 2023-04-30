package com.elicul.azuredevopscommitmessageplugin.listener

import com.elicul.azuredevopscommitmessageplugin.services.CommitMessageService
import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel

class BranchCheckoutListener(
    private val panel: CheckinProjectPanel,
    private val commitMessageService: CommitMessageService
) : BranchChangeListener {

    override fun branchWillChange(branchName: String) {
    }

    override fun branchHasChanged(branchName: String) {
        val commitMessage = commitMessageService.getCommitMessageFromBranchName(branchName)
        panel.commitMessage = commitMessage
    }
}