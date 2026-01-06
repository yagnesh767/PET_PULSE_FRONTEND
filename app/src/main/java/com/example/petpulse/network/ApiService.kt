package com.example.petpulse.network

import com.example.petpulse.model.ForgotPasswordRequest
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.model.LoginRequest
import com.example.petpulse.model.LoginResponse
import com.example.petpulse.model.PetListResponse
import com.example.petpulse.model.ResetPasswordRequest
import com.example.petpulse.model.SignupRequest
import com.example.petpulse.model.SimpleResponse
import com.example.petpulse.model.TimelineResponse
import com.example.petpulse.model.VerifyOtpRequest
import com.example.petpulse.model.VaccineListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login.php")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/register.php")
    fun signup(@Body request: SignupRequest): Call<GeneralResponse>

    @POST("auth/verify_otp.php")
    fun verifyOtp(
        @Body body: Map<String, String>
    ): Call<GeneralResponse>

    @POST("auth/forgot_password.php")
    fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Call<GeneralResponse>

    @POST("auth/resend_otp.php")
    fun resendOtp(
        @Body body: Map<String, String>
    ): Call<GeneralResponse>

    @POST("auth/verify_reset_otp.php")
    fun verifyResetOtp(
        @Body request: VerifyOtpRequest
    ): Call<GeneralResponse>

    @POST("auth/reset_password.php")
    fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Call<GeneralResponse>

    @GET("pets/list_pets.php")
    suspend fun getPets(
        @Query("user_id") userId: Int
    ): PetListResponse

    @Multipart
    @POST("pets/add_pet.php")
    suspend fun addPet(
        @Part("user_id") userId: RequestBody,
        @Part("pet_name") petName: RequestBody,
        @Part("species") species: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("age") age: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<SimpleResponse>

    @GET("pets/get_pet_by_id.php")
    fun getPetById(@Query("pet_id") petId: Int): Call<ResponseBody>

    @GET("pets/pet_overview.php")
    fun getPetOverview(@Query("pet_id") petId: Int): Call<ResponseBody>

    @Multipart
    @POST("pets/update_pet_image.php")
    fun updatePetImage(
        @Part("pet_id") petId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("pets/add_medical_record.php")
    fun addMedicalRecord(
        @Part("pet_id") petId: RequestBody,
        @Part("record_type") recordType: RequestBody,
        @Part("title") title: RequestBody,
        @Part("record_date") recordDate: RequestBody,
        @Part("veterinarian") vet: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<GeneralResponse>

    @GET("pets/list_vaccines.php")
    fun listVaccines(@Query("pet_id") petId: Int): Call<VaccineListResponse>

    @Multipart
    @POST("pets/add_vaccine.php")
    fun addVaccine(
        @Part("pet_id") petId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("record_date") date: RequestBody,
        @Part("status") status: RequestBody,
        @Part("next_due") nextDue: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<GeneralResponse>

    @GET("pets/list_medical_records.php")
    fun getPetTimeline(@Query("pet_id") petId: Int): Call<TimelineResponse>
}
