package com.highstreet.taobaocang.net


/**
 * @version 1.0
 * @describe 后台接口相关
 */
interface Apiservice {



//
//    /**
//     * 登录
//     */
//    @POST("work/user/login")
//    @FormUrlEncoded
//    fun login(@FieldMap map: Map<String, String>): Observable<BaseResponse<UserInfoBean>>
//
//
//    /**
//     * 文件上传
//     */
//    @Multipart
//    @POST("data/attach/uploadFile")
//    fun uploadFile(@Part file: MultipartBody.Part, @Part("diskName") diskName: RequestBody, @Part("fileType") fileType: RequestBody): Observable<BaseResponse<UpFileBean>>
//
//
//
//    @GET("work/task/queryTask")
//    fun queryTask(): Observable<BaseResponse<ArrayList<TaskBean>>>
//
//
//
//    /**
//     * 查询未读消息数量
//     */
//    @GET("work/user/queryNotReadMessage")
//    fun queryNotReadMessage(): Observable<BaseResponse<String>>
//
//
//    /**
//     * 用户点击阅读消息
//     */
//    @POST("work/user/readMessage")
//    @FormUrlEncoded
//    fun readMessage(@Field("notifyId") notifyId: String): Observable<BaseResponse<ArrayList<TaskBean>>>
//
//
//    /**
//     * 查看任务详情  taskId
//     */
//    @POST("work/task/queryTaskInfo")
//    @FormUrlEncoded
//    fun queryTaskInfo(@Field("taskId") taskId: String): Observable<BaseResponse<TaskDetialBean>>
//
//    /**
//     * 查询已完成任务
//     */
//    @POST("work/task/queryFinishTask")
//    fun queryFinishTask(): Observable<BaseResponse<ArrayList<TaskItemBean>>>
//
//    /**
//     * 修改任务完成度  taskId	任务id    progress任务完成度 (数字 10,20,30,40....100)
//     */
//    @POST("work/task/updateTask")
//    @FormUrlEncoded
//    fun updateTask(@Field("taskId") taskId: String, @Field("progress") progress: String): Observable<BaseResponse<UserInfoBean>>
//
//    /**
//     * 添加维修信息  taskId	任务id    repairDesc维修描述   attachId 图片id
//     */
//    @POST("work/task/addRepairDesc")
//    @FormUrlEncoded
//    fun addRepairDesc(@FieldMap map: Map<String, String>): Observable<BaseResponse<UserInfoBean>>
//
//    /**
//     * 查询维修信息
//     */
//    @POST("work/task/queryProgress")
//    @FormUrlEncoded
//    fun queryProgress(@FieldMap map: Map<String, String>): Observable<BaseResponse<RepairInfoBean>>
//
//    /**
//     * 查询用户消息
//     *
//     * pageNo
//     * pageSize
//     */
//    @POST("work/user/queryUserMessage")
//    @FormUrlEncoded
//    fun queryUserMessage(@FieldMap map: Map<String, String>): Observable<BaseResponse<NoticeBean>>
//
//
//    //直播相关
//    /**
//     * 查询用户消息
//     *
//     * pageNo
//     * pageSize
//     * https://live.miaobolive.com/Room/GetHotLive_v2?
//     */
//    @POST("Room/GetHotLive_v2")
//    @FormUrlEncoded
//    fun GetHotLive_v2(@FieldMap map: Map<String, String>): Observable<BaseResponse<ALListBean>>
//
//    /**
//     * 查询用户状态
//     * pageNo
//     * pageSize
//     * https://live.miaobolive.com/Room/GetHotLive_v2?
//     */
//    @GET()
//    fun live_room_userinfo(@Url url:String ,@QueryMap map: Map<String, String>): Observable<LiveUserInfo>
}