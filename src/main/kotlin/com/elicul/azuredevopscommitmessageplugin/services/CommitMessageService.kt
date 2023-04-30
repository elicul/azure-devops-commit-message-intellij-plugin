package com.elicul.azuredevopscommitmessageplugin.services

import com.intellij.openapi.Disposable
import java.util.*
import java.util.regex.Pattern

private const val DEVOPS_BRANCH_REGEX = "[0-9]+"
private const val PREFIX = "#"
private const val INFIX = " "

class CommitMessageService : Disposable {
    fun getCommitMessageFromBranchName(branchName: String?): String {

        if (branchName == null)
            return ""

        val azureNumber = extractAzureDevOpsNumberFromBranch(branchName)
        return if (azureNumber != null)
            createCommitMessage(azureNumber)
        else ""
    }

    private fun extractAzureDevOpsNumberFromBranch(
        branchName: String,
    ): String? {

        val pattern = Pattern.compile(DEVOPS_BRANCH_REGEX).toRegex()
        val azureNumber = pattern.find(branchName)
        return azureNumber?.value
    }

    private fun createCommitMessage(azureNumber: String): String {
        return String.format(
            Locale.US,
            "%s%s%s",
            PREFIX,
            azureNumber,
            INFIX
        )
    }

    override fun dispose() {
    }
}