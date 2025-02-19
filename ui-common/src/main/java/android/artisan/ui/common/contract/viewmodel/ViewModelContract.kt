package android.artisan.ui.common.contract.viewmodel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

public interface ViewModelContract<S, I> {
    public val uiState: StateFlow<S>

    public fun processIntent(intent: I)

    public companion object {
        public val startWithFiveSecStopTimeout: SharingStarted = SharingStarted.WhileSubscribed(5000)
    }
}
