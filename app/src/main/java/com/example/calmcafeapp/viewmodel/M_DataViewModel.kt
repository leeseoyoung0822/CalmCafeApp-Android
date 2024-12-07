import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calmcafeapp.api.M_DataService
import com.example.calmcafeapp.api.M_HomeService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.CongestionDataResponse
import com.example.calmcafeapp.data.CongestionDataResult
import com.example.calmcafeapp.data.FavoriteDataResponse
import com.example.calmcafeapp.data.FavoriteDataResult
import com.example.calmcafeapp.data.VisitDataResponse
import com.example.calmcafeapp.data.VisitDataResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MDataViewModel(application: Application) : AndroidViewModel(application) {

    private val mDataService: M_DataService = ApiManager.mDataService

    private val _visitData = MutableLiveData<VisitDataResult>()
    val visitData: LiveData<VisitDataResult> get() = _visitData

    private val _favoriteData = MutableLiveData<FavoriteDataResult>()
    val favoriteData: LiveData<FavoriteDataResult> get() = _favoriteData

    private val _congestionData = MutableLiveData<CongestionDataResult>()
    val congestionData: LiveData<CongestionDataResult> get() = _congestionData

    fun fetchVisitData() {
        mDataService.getVisitData().enqueue(object : Callback<VisitDataResponse> {
            override fun onResponse(call: Call<VisitDataResponse>, response: Response<VisitDataResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _visitData.value = response.body()?.result
                }
            }

            override fun onFailure(call: Call<VisitDataResponse>, t: Throwable) {
                // 에러 처리
            }
        })
    }

    fun fetchFavoriteData() {
        mDataService.getFavoriteData().enqueue(object : Callback<FavoriteDataResponse> {
            override fun onResponse(call: Call<FavoriteDataResponse>, response: Response<FavoriteDataResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _favoriteData.value = response.body()?.result
                }
            }

            override fun onFailure(call: Call<FavoriteDataResponse>, t: Throwable) {
                // 에러 처리
            }
        })
    }

    fun fetchCongestionData() {
        mDataService.getCongestionData().enqueue(object : Callback<CongestionDataResponse> {
            override fun onResponse(call: Call<CongestionDataResponse>, response: Response<CongestionDataResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    _congestionData.value = response.body()?.result
                }
            }

            override fun onFailure(call: Call<CongestionDataResponse>, t: Throwable) {
                // 에러 처리
            }
        })
    }
}
