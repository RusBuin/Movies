package com.loc.newsapp.domain.usecases.app_entry

import com.loc.newsapp.domain.manger.LocalUserManger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadAppEntryUseCase @Inject constructor(
    private val localUserManger: LocalUserManger
) {

    operator fun invoke(): Flow<Boolean> {
        return localUserManger.readAppEntry()
    }
}