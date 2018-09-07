package com.example.mohamed.git_task;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitService {
    /*@GET("users")
    Call<ResponseBody> getUsers();
*/

    @GET("repos")
    Call<ResponseBody>getPosts(@Query("page")int pageNumber,@Query("per_page")int perPage);

}
