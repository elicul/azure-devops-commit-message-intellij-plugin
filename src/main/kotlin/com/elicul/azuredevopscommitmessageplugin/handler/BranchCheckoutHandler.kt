package com.elicul.azuredevopscommitmessageplugin.handler

import com.elicul.azuredevopscommitmessageplugin.listener.BranchCheckoutListener
import com.elicul.azuredevopscommitmessageplugin.services.CommitMessageService
import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.BranchChangeListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory

class BranchCheckoutHandler : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        val commitMessageService = panel.project.service<CommitMessageService>()
        val messageBus = panel.project.messageBus.connect(commitMessageService)
        messageBus.subscribe(BranchChangeListener.VCS_BRANCH_CHANGED, BranchCheckoutListener(panel, commitMessageService))
        return object : CheckinHandler() {}
    }
}