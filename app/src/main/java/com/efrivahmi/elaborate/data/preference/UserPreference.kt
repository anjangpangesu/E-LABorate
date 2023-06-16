package com.efrivahmi.elaborate.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.efrivahmi.elaborate.data.model.Diagnose
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.response.DResponse
import com.efrivahmi.elaborate.data.response.VerifyCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERID_KEY = stringPreferencesKey("userId")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("confirmPassword")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val RESET_TOKEN_KEY = stringPreferencesKey("resetToken")
        private val DIAGNOSIS_ID_KEY = stringPreferencesKey("diagnosisId")
        private val PREDICTION_KEY = intPreferencesKey("prediction")
        private val AGE_KEY = intPreferencesKey("age")
        private val SEX_KEY = intPreferencesKey("sex")
        private val RBC_KEY = doublePreferencesKey("rbc")
        private val HGB_KEY = doublePreferencesKey("hgb")
        private val HCT_KEY = doublePreferencesKey("hct")
        private val MCV_KEY = doublePreferencesKey("mcv")
        private val MCH_KEY = doublePreferencesKey("mch")
        private val MCHC_KEY = doublePreferencesKey("mchc")
        private val RDW_CV_KEY = doublePreferencesKey("rdw_cv")
        private val WBC_KEY = doublePreferencesKey("wbc")
        private val NEU_KEY = doublePreferencesKey("neu")
        private val LYM_KEY = doublePreferencesKey("lym")
        private val MO_KEY = doublePreferencesKey("mo")
        private val EOS_KEY = doublePreferencesKey("eos")
        private val BA_KEY = doublePreferencesKey("ba")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERID_KEY] ?:"",
                preferences[USERNAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[TOKEN_KEY] ?:"",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = user.userId
            preferences[USERNAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveResetToken(verifyCode: VerifyCode) {
        dataStore.edit { preferences ->
            preferences[RESET_TOKEN_KEY] = verifyCode.resetToken
            preferences[USERID_KEY] = verifyCode.userId
            preferences[USERNAME_KEY] = verifyCode.username
            preferences[EMAIL_KEY] = verifyCode.email
        }
    }

    suspend fun saveDiagnose(dResponse: DResponse) {
        dataStore.edit { preference ->
            preference[DIAGNOSIS_ID_KEY] = dResponse.diagnosisId
            preference[PREDICTION_KEY] = dResponse.prediction
        }
    }

    suspend fun saveDiagnoseRequest(diagnose: Diagnose) {
        dataStore.edit { preferences ->
            preferences[AGE_KEY] = diagnose.age
            preferences[SEX_KEY] = diagnose.sex
            preferences[RBC_KEY] = diagnose.rbc
            preferences[HGB_KEY] = diagnose.hgb
            preferences[HCT_KEY] = diagnose.hct
            preferences[MCV_KEY] = diagnose.mcv
            preferences[MCH_KEY] = diagnose.mch
            preferences[MCHC_KEY] = diagnose.mchc
            preferences[RDW_CV_KEY] = diagnose.rdw_cv
            preferences[WBC_KEY] = diagnose.wbc
            preferences[NEU_KEY] = diagnose.neu
            preferences[LYM_KEY] = diagnose.lym
            preferences[MO_KEY] = diagnose.mo
            preferences[EOS_KEY] = diagnose.eos
            preferences[BA_KEY] = diagnose.ba
        }
    }

    fun getSavedDiagnose(): Flow<Diagnose> {
        return dataStore.data.map { preferences ->
            Diagnose(
                preferences[AGE_KEY]?: 0,
                preferences[SEX_KEY]?: 0,
                preferences[RBC_KEY]?: 0.0,
                preferences[HGB_KEY]?: 0.0,
                preferences[HCT_KEY]?: 0.0,
                preferences[MCV_KEY]?: 0.0,
                preferences[MCH_KEY]?: 0.0,
                preferences[MCHC_KEY]?: 0.0,
                preferences[RDW_CV_KEY]?: 0.0,
                preferences[WBC_KEY]?: 0.0,
                preferences[NEU_KEY]?: 0.0,
                preferences[LYM_KEY]?: 0.0,
                preferences[MO_KEY]?: 0.0,
                preferences[EOS_KEY]?: 0.0,
                preferences[BA_KEY]?: 0.0
            )
        }
    }

    fun getDiagnose(): Flow<DResponse> {
        return dataStore.data.map { preferences ->
            DResponse(
                preferences[DIAGNOSIS_ID_KEY]?: "",
                preferences[PREDICTION_KEY]?: 0
            )
        }
    }

    fun getResetToken(): Flow<VerifyCode> {
        return dataStore.data.map { preferences ->
            VerifyCode(
                200,
                false,
                "Token generated",
                preferences[USERID_KEY] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[RESET_TOKEN_KEY] ?: "",
                true
            )
        }
    }
}