package com.elicul.azuredevopscommitmessageplugin.action

import com.elicul.azuredevopscommitmessageplugin.services.CommitMessageService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.openapi.vcs.ui.Refreshable
import git4idea.GitUtil

class PluginAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val currentProject = actionEvent.project

        if (currentProject != null) {
            val plugin = currentProject.service<CommitMessageService>()

            val repositoryManager = GitUtil.getRepositoryManager(currentProject)
            val branch = repositoryManager.repositories[0].currentBranch

            val commitPanel = getCommitPanel(actionEvent)
            val newCommitMessage = plugin.applyBranchNameToCommitMessage(branch?.name, getCurrentCommitMessage(actionEvent))
            commitPanel?.setCommitMessage(newCommitMessage)
        }
    }

    private fun getCurrentCommitMessage(actionEvent: AnActionEvent): String? {
        val panel = Refreshable.PANEL_KEY.getData(actionEvent.dataContext)
        if (panel is CheckinProjectPanel) {
            return panel.commitMessage
        }

        val commitMessageControl = VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEvent.dataContext)
        if (commitMessageControl is CommitMessage) {
            return commitMessageControl.comment
        }

        return null
    }

    private fun getCommitPanel(actionEvent: AnActionEvent): CommitMessageI? {
        val data = Refreshable.PANEL_KEY.getData(actionEvent.dataContext)

        if (data is CommitMessageI) {
            return data
        }

        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEvent.dataContext)
    }
}
