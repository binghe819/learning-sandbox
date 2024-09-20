package com.binghe.i_continuation

import com.binghe.a_routine_coroutine.printWithThread
import kotlinx.coroutines.delay

/**
 * 코루틴은 어떻게 어떤 지짐에서 중단도하고 재개도 하는 것일까?
 * ->
 */
fun main() {

}

// 라벨을 갖고 있는 인터페이스
interface Continuation {
    
}

/**
 * 실제 우리가 사용하는 코드
 */
class UserService {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    suspend fun findUser(userId: Long): UserDto {
        // 중단될 수 있는 0단계 - 초기 시작
        printWithThread("프로필을 가져온다.")
        val profile = userProfileRepository.findProfile(userId)

        // 중단될 수 있는 1단계 - 1차 중단 후 재시작
        printWithThread("이미지를 가져온다.")
        val image = userImageRepository.findImage(profile)

        // 중단될 수 있는 2단계 - 2차 중단 후 재시작
        return UserDto(profile, image)
    }
}

/**
 * 코루틴이 어떻게 중단하고 재개하는지 설명하기 위해 작성된 코드
 */
class UserServiceContinuation {
    private val userProfileRepository = UserProfileRepository()
    private val userImageRepository = UserImageRepository()

    suspend fun findUser(userId: Long): UserDto {
        // state machine
        val sm = object : Continuation {
            var label = 0 // 익명 클래스를 만들어 라벨을 갖게 만든다
            var profile: Profile? = null
            var image: Image? = null
        }

        when (sm.label) {
            0 -> {
                printWithThread("프로필을 가져온다.")
                sm.label = 1
                val profile = userProfileRepository.findProfile(userId)
                sm.profile = profile
            }

            1 -> {
                printWithThread("이미지를 가져온다.")
                sm.label = 2
                val image = userImageRepository.findImage(sm.profile!!)
                sm.image = image
            }

             2 -> {
                 return UserDto(sm.profile!!, sm.image!!)
             }
        }
    }
}

class UserProfileRepository {
    suspend fun findProfile(userId: Long): Profile {
        delay(100L)
        return Profile()
    }
}

class UserImageRepository {
    suspend fun findImage(profile: Profile): Image {
        delay(100L)
        return Image()
    }
}

data class UserDto(
    val profile: Profile,
    val image: Image
)

class Profile

class Image
