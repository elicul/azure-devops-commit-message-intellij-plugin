package com.elicul.azuredevopscommitmessageplugin.services

import com.intellij.openapi.Disposable
import java.util.*
import java.util.regex.Pattern

private const val DEVOPS_BRANCH_REGEX = "[0-9]+"
private const val PREFIX = "[#"
private const val INFIX = "] "
private const val PREFIX_REGEX = "^\\[#\\d+\\]\\s*"

class CommitMessageService : Disposable {
    fun applyBranchNameToCommitMessage(branchName: String?, currentMessage: String?): String {
        val azureNumber = branchName?.let { extractAzureDevOpsNumberFromBranch(it) }
        val sanitizedMessage = sanitizeCommitMessage(currentMessage)

        return if (azureNumber != null) {
            createCommitMessagePrefix(azureNumber) + sanitizedMessage
        } else {
            sanitizedMessage
        }
    }

    private fun extractAzureDevOpsNumberFromBranch(
        branchName: String,
    ): String? {

        val pattern = Pattern.compile(DEVOPS_BRANCH_REGEX).toRegex()
        val azureNumber = pattern.find(branchName)
        return azureNumber?.value
    }

    private fun sanitizeCommitMessage(currentMessage: String?): String {
        if (currentMessage.isNullOrBlank()) {
            return ""
        }

        return currentMessage.replace(PREFIX_REGEX.toRegex(), "")
    }

    private fun createCommitMessagePrefix(azureNumber: String): String {
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
