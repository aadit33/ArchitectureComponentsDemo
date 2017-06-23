package it.codingjam.github.ui.common

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.support.v4.app.FragmentActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import it.codingjam.github.util.UiActionsLiveData
import it.codingjam.github.util.ViewStateLiveData
import org.reactivestreams.Publisher

open class RxViewModel<VS>(initialState: VS) : ViewModel() {

    private val clearedSubject: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    protected val cleared: Publisher<Boolean> = clearedSubject.toFlowable(BackpressureStrategy.BUFFER)

    val liveData = ViewStateLiveData(initialState)

    protected var state: VS by liveData

    protected val uiActions = UiActionsLiveData()

    override fun onCleared() = clearedSubject.onNext(true)

    fun observeUiActions(owner: LifecycleOwner, executor: ((FragmentActivity) -> Unit) -> Unit) {
        uiActions.observe(owner, executor)
    }

    fun observeUiActionsForever(executor: ((FragmentActivity) -> Unit) -> Unit) {
        uiActions.observeForever(executor)
    }
}