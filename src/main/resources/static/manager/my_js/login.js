/**
 * Created by linziyu on 2019/2/9.
 */

// 登录处理
layui.use(['form', 'layer', 'jquery'], function () {


    var form = layui.form;
    var $ = layui.jquery;

    form.on('submit(login)', function (data) {
        var username = $('#username').val();
        var password = $('#password').val();
        // layer.msg(username);
        $.ajax({

            url: "/manage/login",
            data: {
                "username": username,
                "password": password
            },
            dataType: "json",
            type: 'post',
            async: false,
            success: function (data) {
                if (data.status == 400) {
                    layer.alert(data.message);
                }
                if (data.status == 200) {
                    window.location.href = "/manage";
                }

            }
        });

    })

});


