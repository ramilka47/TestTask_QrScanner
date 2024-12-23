package ru.ramil.customqrscanner.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators : Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        checkNotNull(modelClass.getCreator()) {
            "unknown model class $modelClass"
        }.get() as T

    private fun <T : ViewModel> Class<T>.getCreator(): Provider<out ViewModel>? =
        creators[this] ?: creators.firstNotNullOf {
            if (this.isAssignableFrom(it.key)) {
                it.value
            } else
                null
        }
}
