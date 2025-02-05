package ee.carlrobert.codegpt.credentials

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread

object CredentialsStore {

    private val credentialsMap = mutableMapOf<CredentialKey, String?>()

    @JvmStatic
    @RequiresBackgroundThread
    fun getCredential(key: CredentialKey): String? =
        credentialsMap.getOrPut(key) {
            PasswordSafe.instance.getPassword(
                CredentialAttributes(
                    generateServiceName("CodeGPT", key.name)
                )
            ) ?: ""
        }.takeIf { !it.isNullOrEmpty() }

    fun setCredential(key: CredentialKey, password: String?) {
        val prevPassword = credentialsMap[key]
        credentialsMap[key] = password

        if (prevPassword != password) {
            val credentialAttributes =
                CredentialAttributes(generateServiceName("CodeGPT", key.name))
            PasswordSafe.instance.setPassword(credentialAttributes, password)
        }
    }

    fun isCredentialSet(key: CredentialKey): Boolean = !getCredential(key).isNullOrEmpty()

    sealed class CredentialKey {
        abstract val name: String

        data object CodeGptApiKey : CredentialKey() {
            override val name: String = "CODEGPT_API_KEY"
        }

        data object OpenaiApiKey : CredentialKey() {
            override val name: String = "OPENAI_API_KEY"
        }

        data object CustomServiceApiKey : CredentialKey() {
            override val name: String = "CUSTOM_SERVICE_API_KEY"
        }

        data object AnthropicApiKey : CredentialKey() {
            override val name: String = "ANTHROPIC_API_KEY"
        }

        data object AzureOpenaiApiKey : CredentialKey() {
            override val name: String = "AZURE_OPENAI_API_KEY"
        }

        data object AzureActiveDirectoryToken : CredentialKey() {
            override val name: String = "AZURE_ACTIVE_DIRECTORY_TOKEN"
        }

        data object LlamaApiKey : CredentialKey() {
            override val name: String = "LLAMA_API_KEY"
        }

        data object GoogleApiKey : CredentialKey() {
            override val name: String = "GOOGLE_API_KEY"
        }

        data object OllamaApikey : CredentialKey() {
            override val name: String = "OLLAMA_API_KEY"
        }
    }
}