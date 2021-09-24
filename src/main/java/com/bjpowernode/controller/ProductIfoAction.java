package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
@CrossOrigin
@Controller
@RequestMapping(value = "/prod")
public class ProductIfoAction {

    //    每页显示的记录数
    public static final int PAGE_SIZE = 5;
    String saveFileName = "";

    @Autowired
    private ProductInfoService productInfoService;

    //    显示全部商品不分页
    @RequestMapping(value = "/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    //    显示第1页的5条记录
    @RequestMapping(value = "/split")
    public String split(HttpServletRequest request) {
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null){
            info = productInfoService.splitPageVo((ProductInfoVo)vo,PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }else{
            info = productInfoService.splitPage(1,PAGE_SIZE);
        }
        request.setAttribute("info", info);
        return "product";
    }

    //    ajax分页的翻页处理
    @ResponseBody
    @RequestMapping(value = "/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo,HttpSession session) {
        PageInfo info = productInfoService.splitPageVo(vo,PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //
    @ResponseBody
    @RequestMapping(value = "/ajaxImg")
//    MultipartFile参数的名称必须和标签的name一样
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
//        提取生成文件名UUID+上传图片的后缀
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
//        得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");
//        转存
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
//       返回客户端JSON对象，封装图片的路径，为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }

    @RequestMapping(value = "/save")
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            request.setAttribute("msg", "增加成功!");
        } else {
            request.setAttribute("msg", "增加失败!");
        }
//        清空saveFileName变量中的内容，为了下次增加或修改的异步Ajax的上传处理
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping(value = "/one")
    public String one(int pid, Model model,ProductInfoVo vo,HttpSession session) {
        ProductInfo info = productInfoService.getByID(pid);
        model.addAttribute("prod", info);
        session.setAttribute("prodVo",vo);
        return "update";
    }

    @RequestMapping(value = "/update")
    public String update(ProductInfo info, HttpServletRequest request) {
//        因为Ajax的异步图片上传，如果上传过，则saveFileName有上传上来的名称，
//        如果没有使用异步ajax上传过图片，则saveFileName为空
//        实体类info使用隐藏表单域提交过来的pImage原始图片名称
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
//        完成更新
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
//            此时说明更新成功
            request.setAttribute("msg", "更新成功");
        } else {
//            更新失败
            request.setAttribute("msg", "更新失败");
        }
//        处理完更新后，saveFileName里面可能有数据，而下一次更新时要使用这个变量作为判断依据
//        就会出错，所以必须清空saveFileName
        saveFileName = "";
        return "forward:/prod/split.action";
    }
    @RequestMapping(value = "/delete")
    public String delete(int pid,HttpServletRequest request,ProductInfoVo vo){

        int num=-1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg","删除成功!");
            request.getSession().setAttribute("deleteProdVo",vo);
        }else{
            request.setAttribute("msg","删除失败!");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }
    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=utf-8")
    public Object deleteAjaxSplit(HttpServletRequest request){
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null){
            info = productInfoService.splitPageVo((ProductInfoVo)vo,PAGE_SIZE );
        }else {
            info = productInfoService.splitPage(1,PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

//    批量删除商品
    @RequestMapping(value = "/deleteBatch")
    public String deleteBatch(String pids,HttpServletRequest request){
        String[] ps = pids.split(",");
        try {
            int num=productInfoService.deleteBatch(ps);
            if (num>0){
                request.setAttribute("msg","批量删除成功!");
            }else {
                request.setAttribute("msg","批量删除失败!");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除!");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }
}
