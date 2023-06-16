package com.efrivahmi.elaborate.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.efrivahmi.elaborate.data.api.ApiConfig
import com.efrivahmi.elaborate.data.api.ml.ApiConfigMl
import com.efrivahmi.elaborate.data.api.ml.DataSourceDiagnose
import com.efrivahmi.elaborate.data.repository.DataSource
import com.efrivahmi.elaborate.data.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun labRepository(context: Context): DataSource {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getConfigApi()
        return DataSource.getInstance(preferences, apiService)
    }

    fun labDiagnose(context: Context): DataSourceDiagnose {
        val preferenceMl = UserPreference.getInstance(context.dataStore)
        val apiServiceMl = ApiConfigMl.getDiagnose()
        return DataSourceDiagnose.getInstance(preferenceMl, apiServiceMl)
    }
}