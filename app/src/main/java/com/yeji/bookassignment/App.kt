package com.yeji.bookassignment

import android.app.Application
import com.yeji.domain.repository.ApiRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    /**
     * comments
     * TODO: DI
     * - serviceLocator
     * (https://en.wikipedia.org/wiki/Service_locator_pattern)
     *  - DI 주입 때문에 이런식으로 사용한다.
     *    - DI 사용하지 않을 시 object 타입의 ApiRepository() 만들어두면 사용하지 않는데 메모리 차지하기에 이렇게 사용하지 않는 것이 좋다.
     *    - DI 사용할 때는 DI에서 알아서 안쓰는 인스턴스들 메모리 관리 해줘서 해당 방법으로 작성해도 괜찮다.
     */
    // serviceLocator
//     val repository = ApiRepository()
}