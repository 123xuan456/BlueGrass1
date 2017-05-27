package com.reeching.utils;

public class HttpApi {
    // ip
//    public final static String ip = "http://192.168.3.110:8080/jeesite/f";
//    public final static String picip = "http://192.168.3.110:8080";

    //	 ip

     public final static String ip = "http://203.86.53.42/ysq/f";
      public final static String picip = "http://203.86.53.42";

    // 获取所有画廊name
    public final static String getallhualangname = "/gallery/galleryInter/galleryInfos";
    // 获取所有展览
    public final static String getallzhanlan = "/exhibition/exhibitionInter/exhibitionInfos";
    // 获取所有画廊
    public final static String getallhualanginfo = "/gallery/galleryInter/getGallerys";
    // 坐标查询画廊
    public final static String findhualang = "/gallery/galleryInter/findGalleryByXYZ";
    // 画廊历史
    public final static String gethistory = "/exhibition/exhibitionInter/hisExhibitions";
    // 上报人
    public final static String reportman = "/gallery/galleryInter/selectUsers";
    // 待检查
    public final static String waitforcheck = "/exhibition/exhibitionInter/checkExhibitions";
    // 待核查
    public final static String waitforhecha = "/exhibition/exhibitionInter/recheckExhibitions";
    // 已完成
    public final static String welldone = "/exhibition/exhibitionInter/finishedExhibitions";
    // 提交待检查
    public final static String reportwaitforcheck = "/exhibition/exhibitionInter/saveCheckInfos";
    // 保存、修改画廊信息
    public final static String keeporalterhualang = "/gallery/galleryInter/saveGallerys";
    // 待核查提交
    public final static String reportwaitforhecha = "/exhibition/exhibitionInter/saveRecheckInfos";
    // 统计
    public final static String count = "/exhibition/exhibitionInter/countInfo";
    // 个人信息
    public final static String personalinfo = "/gallery/galleryInter/getUserInfos";
    // 登录
    public final static String login = "/gallery/galleryInter/login";
    // 上报画廊
    public final static String reporthualang = "/exhibition/exhibitionInter/saveExhibitions";
    // 版本更新
    public final static String updata = "/gallery/galleryInter/isAppUpdate";
    // 根据名称查坐标
    public final static String nametolng = "/gallery/galleryInter/findGalleryByName";
    //正在展览
    public final static String show = "/exhibition/exhibitionInter/beingExhibiton";
    //计划展览
    public final static String plan = "/exhibition/exhibitionInter/willExhibition";
    //历史展览
    public final static String allHistory = "/exhibition/exhibitionInter/historyExhibitons";
    //已核查
    public final static String havecheck = "/exhibition/exhibitionInter/alreadyCheck";
    //已经核查
    public final static String haveverification = "/exhibition/exhibitionInter/alreadyRecheck";
    //删除画廊
    public final static String deleteHuaLang = "/gallery/galleryInter/delGallery";
    //得到删除画廊的列表
    public final static String getdelHuangLang = "/gallery/galleryInter/delGalleryList";
    // 删除画廊历史
    public final static String delhistory = "/exhibition/exhibitionInter/hisExhibitions1";
    //问题展览
    public final static String matter = "/exhibition/exhibitionInter/exhibitionQuestion";
    //获取下达人
    public final static String xiadapeople = "/exhibition/exhibitionInter/findUser";
    //任務下達
    public final static String taskxiada = "/exhibition/exhibitionInter/saveTaskAssignment";
}
