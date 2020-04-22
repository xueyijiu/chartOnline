layui.use(["form", "table", "element","laydate"], function () {
    var $ = layui.jquery;
    var element = layui.element;
    var table = layui.table;
    var form = layui.form;
    $("#display-users").click(function () {
        table.render({
            elem: '#test'
            , url: '/manage/getAllUser'
            , title: '用户数据表'
            , skin: 'row' //表格风格
            , even: true
            , page: true //是否显示分页
            , limits: [5, 10, 20]
            , limit: 10 //每页默认显示的数量
            , done: function (res) {
                userPage.data = res.data;
            }
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'id', title: 'ID'}
                , {field: 'username', title: '用户名'}
                , {field: 'password', title: '密码'}
                , {field: 'role', title: '角色'}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 500}
            ]]
        });
    });

    $("#display-dynamic").click(function () {
        table.render({
            elem: '#test'
            , url: '/manage/user-dynamic/dynamicList'
            , title: '用户动态表'
            , toolbar: '#toolbarDynic'
            , skin: 'row' //表格风格
            , even: true
            , page: true //是否显示分页
            , limits: [5, 10, 20]
            , limit: 10 //每页默认显示的数量
            , done: function (res) {
                console.info(res)
                userPage.data = res.data;
            }
            , cols: [[
                // {type: 'checkbox', fixed: 'left'}
                  {field: 'id', title: 'ID'}
                , {field: 'username', title: '用户名'}
                , {field: 'dynaminContent', title: '动态内容'}
                , {field: 'createTime', title: '发布时间'}
                , {field: 'deleteStatusString', title: '是否删除'}
                , {fixed: 'right', title: '操作', toolbar: '#dynamicBar', width: 500}
            ]]
        });
    });

    $("#display-comment").click(function () {
        table.render({
            elem: '#test'
            , url: '/manage/comment/queryCommentList'
            , toolbar: '#toolbarDemoComment'
            , title: '用户评论表'
            , skin: 'row' //表格风格
            , even: true
            , page: true //是否显示分页
            , limits: [5, 10, 20]
            , limit: 10 //每页默认显示的数量
            , done: function (res) {
                console.info(res)
                userPage.data = res.data;
            }
            , cols: [[
                // {type: 'checkbox', fixed: 'left'}
                , {field: 'id', title: 'ID'}
                , {field: 'commentUser', title: '用户名'}
                , {field: 'commentContent', title: '评论内容'}
                , {field: 'createTime', title: '发布时间'}
                , {fixed: 'right', title: '操作', toolbar: '#commentBar', width: 500}
            ]]
            , page: true
            , limit: 5
        });
    });

    $("#display-violation").click(function () {
        table.render({
            elem: '#test'
            , url: '/manage/control-table/violationList'
            , title: '违规字段表'
            ,toolbar: '#topBar'
            , skin: 'row' //表格风格
            , even: true
            , page: true //是否显示分页
            , limits: [3, 5, 10]
            , limit: 2 //每页默认显示的数量
            , done: function (res) {
                console.info(res)
                userPage.data = res.data;
            }
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'id', title: 'ID'}
                , {field: 'content', title: '内容'}
                , {field: 'statusString', title: '状态'}
                , {fixed: 'right', title: '操作', toolbar: '#bottomBar', width: 500}
            ]]
            , page: true
            , limit: 5
        });
    });

    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;//获取User数据
        var role = data.role;
        if (obj.event === 'del') {//对应删除事件
            layer.confirm('真的删除该用户吗', function (index) {
                $.ajax({
                    url: '/manage/deleteUserById',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id: data.id,
                        role: data.role
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            layer.alert("删除成功！", function () {
                                window.parent.location.reload();//刷新父页面
                                parent.layer.close(index);//关闭弹出层
                            });
                        } else if (data.code == 500) {
                            layer.alert("您不是管理员，没有权限进行该操作");
                        } else if (data.code == 501) {
                            layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                        }
                    }
                });
                layer.close(index);
            })
        } else if (obj.event === 'del1') {//对应删除事件
            layer.confirm('真的删除该动态吗', function (index) {
                $.ajax({
                    url: '/manage/user-dynamic/updateDyStatus',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id: data.id,status:1
                    },
                    success: function (data) {
                        if (data.status == 200) {
                            layer.alert("删除成功！", function () {
                                window.parent.location.reload();//刷新父页面
                                parent.layer.close(index);//关闭弹出层
                            });
                        } else if (data.status == 500) {
                            layer.alert("您不是管理员，没有权限进行该操作");
                        } else if (data.status == 501) {
                            layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                        }
                    }
                });
                layer.close(index);
                return;
            });
        }else if (obj.event === 'restore') {//对应删除事件
            layer.confirm('真的恢复该动态吗', function (index) {
                $.ajax({
                    url: '/manage/user-dynamic/updateDyStatus',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id: data.id,status:0
                    },
                    success: function (data) {
                        if (data.status == 200) {
                            layer.alert("恢复成功！", function () {
                                window.parent.location.reload();//刷新父页面
                                parent.layer.close(index);//关闭弹出层
                            });
                        } else if (data.status == 500) {
                            layer.alert("您不是管理员，没有权限进行该操作");
                        } else if (data.status == 501) {
                            layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                        }
                    }
                });
                layer.close(index);
                return;
            });
            restore
        }
        else if (obj.event === 'getInfo1') {
            layer.open({
                type: 2,//坑
                title: '查看动态',
                area: ['500px', '500px'],
                content: '/manage/display_dynamic_info',
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    // 获取子页面的iframe
                    var iframe = window['layui-layer-iframe' + index];
                    // 向子页面的全局函数child传参
                    // iframe.get(data.name);
                    console.info(data);
                    body.find("#volume").html(data.username);
                    body.find("#date").html(data.createTime);
                    body.find("#image").attr("src", data.dynamicFiles[0].filePath);
                    body.find("#content").html(data.dynaminContent);
                    // body.find("#role").val(data.role);
                    form.render();
                }
            });
        } else if (obj.event === 'del') {//对应删除事件
            layer.confirm('真的删除该用户吗', function (index) {
                $.ajax({
                    url: '/manage/deleteUserById',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id: data.id,
                        role: data.role
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            layer.alert("删除成功！", function () {
                                window.parent.location.reload();//刷新父页面
                                parent.layer.close(index);//关闭弹出层
                            });
                        } else if (data.code == 500) {
                            layer.alert("您不是管理员，没有权限进行该操作");
                        } else if (data.code == 501) {
                            layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                        }
                    }
                });
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            layer.open({
                type: 2,//坑
                title: '查看用户',
                area: ['auto', 'auto'],
                content: '/manage/change_user_role',
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    // 获取子页面的iframe
                    var iframe = window['layui-layer-iframe' + index];
                    // 向子页面的全局函数child传参
                    // iframe.get(data.name);
                    body.find("#id").val(data.id);
                    form.render();
                }
            });
        } else if (obj.event === 'getInfo') {
            layer.open({
                type: 2,//坑
                title: '查看用户',
                area: ['500px', '500px'],
                content: '/manage/display_user_info',
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    // 获取子页面的iframe
                    var iframe = window['layui-layer-iframe' + index];
                    // 向子页面的全局函数child传参
                    // iframe.get(data.name);
                    body.find("#username").val(data.username);
                    body.find("#password").val(data.password);
                    body.find("#id").val(data.id);
                    body.find("#role").val(data.role);
                    form.render();
                }
            });
        } else if (obj.event == 'delComment') {
            layer.confirm('真的删除该评论吗?', function (index) {
                $.ajax({
                    url: '/manage/comment/deleteComment',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id: data.id,
                    },
                    success: function (data) {
                        if (data.status == 200) {
                            layer.alert("删除成功！", function () {
                                window.parent.location.reload();//刷新父页面
                                parent.layer.close(index);//关闭弹出层
                            });
                        } else if (data.status == 500) {
                            layer.alert("您不是管理员，没有权限进行该操作");
                        } else if (data.status == 501) {
                            layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                        }
                    }
                });
                layer.close(index);
            });
        }
        else if(obj.event=='updateViolation'){
            layer.open({
                type: 2,//坑
                title: '更新违规字段',
                area: ['500px', '500px'],
                content: '/manage/control-table/updateViolation2',
                success: function (layero, index) {
                    var body = layui.layer.getChildFrame('body', index);
                    // 获取子页面的iframe
                    var iframe = window['layui-layer-iframe' + index];
                    // 向子页面的全局函数child传参
                    // iframe.get(data.name);
                    body.find("#content").val(data.content);
                    body.find("#status").val(data.status);
                    body.find("#verId").val(data.id);
                    form.render();
                }
            });
        }
        else if(obj.event=='delViolation'){
            layer.confirm('真的删除该字段吗', function (index) {
                $.ajax({
                    url: '/manage/control-table/deleteViolation',
                    type: 'post',
                    dataType: 'json',
                    data:{id:data.id},
                    success: function (data) {
                        if (data.status == 200) {
                            layer.alert("删除成功！", function () {
                                window.parent.location.reload();//刷新父页面
                                parent.layer.close(index);//关闭弹出层
                            });
                        } else if (data.status == 500) {
                            layer.alert("您不是管理员，没有权限进行该操作");
                        } else if (data.status == 501) {
                            layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                        }
                    }
                });
                layer.close(index);
            });
        }

    });
    //头工具栏事件
    table.on('toolbar(test)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'getCommentInfo':
                table.render({
                    elem: '#test'
                    , url: '/manage/comment/queryCommentList'
                    , toolbar: '#toolbarDemoComment'
                    , title: '用户评论表'
                    ,where: {
                        message: $("#message").val(),
                        username: $("#username").val()
                    }
                    , skin: 'row' //表格风格
                    , even: true
                    , page: true //是否显示分页
                    , limits: [5, 10, 20]
                    , limit: 10 //每页默认显示的数量
                    , done: function (res) {
                        console.info(res)
                        userPage.data = res.data;
                    }
                    , cols: [[
                        // {type: 'checkbox', fixed: 'left'}
                        , {field: 'id', title: 'ID'}
                        , {field: 'commentUser', title: '用户名'}
                        , {field: 'commentContent', title: '评论内容'}
                        , {field: 'createTime', title: '发布时间'}
                        , {fixed: 'right', title: '操作', toolbar: '#commentBar', width: 500}
                    ]]
                    , page: true
                });
                break;
            case 'insertInfo':
                layer.open({
                    type: 2,//坑
                    title: '增加违规字段',
                    area: ['500px', '500px'],
                    content: '/manage/control-table/violation1',
                    success: function (layero, index) {
                        var body = layui.layer.getChildFrame('body', index);
                        // 获取子页面的iframe
                        var iframe = window['layui-layer-iframe' + index];
                        // 向子页面的全局函数child传参
                        // iframe.get(data.name);
                        body.find("#id").val(data.id);
                        form.render();
                    }
                });
                break;
            // case 'updateViolation':{
            //     layer.open({
            //         type: 2,//坑
            //         title: '更新违规字段',
            //         area: ['500px', '500px'],
            //         content: '/manage/control-table/updateViolation1',
            //         success: function (layero, index) {
            //
            //             var body = layui.layer.getChildFrame('body', index);
            //             // 获取子页面的iframe
            //             var iframe = window['layui-layer-iframe' + index];
            //             // 向子页面的全局函数child传参
            //             // iframe.get(data.name);
            //             body.find("#id").val(data.id);
            //             form.render();
            //         }
            //     });
            //     break;
            // }
            case 'available':{
                var data = checkStatus.data;
                var s = JSON.stringify(data);
                if(data.length<=0){
                    alert("请选择需要更新的内容!");
                    return;
                }
                layer.confirm('真的更新该字段吗', function (index) {
                    $.ajax({
                        url: '/manage/control-table/updateBachViolation',
                        type: 'post',
                        dataType: 'json',
                        data:{data:s,status:true},
                        success: function (data) {
                            if (data.status == 200) {
                                layer.alert("更新成功！", function () {
                                    window.parent.location.reload();//刷新父页面
                                    parent.layer.close(index);//关闭弹出层
                                });
                            } else if (data.status == 500) {
                                layer.alert("您不是管理员，没有权限进行该操作");
                            } else if (data.status == 501) {
                                layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                            }
                        }
                    });
                    layer.close(index);
                });
                break;
            }
            case 'unavailable':{
                var data = checkStatus.data;
                var s = JSON.stringify(data);
                if(data.length<=0){
                    alert("请选择需要更新的内容!");
                    return;
                }
                layer.confirm('确认更新该字段吗', function (index) {
                    $.ajax({
                        url: '/manage/control-table/updateBachViolation',
                        type: 'post',
                        dataType: 'json',
                        data:{data:s,status:false},
                        success: function (data) {
                            if (data.status == 200) {
                                layer.alert("更新成功！", function () {
                                    window.parent.location.reload();//刷新父页面
                                    parent.layer.close(index);//关闭弹出层
                                });
                            } else if (data.status == 500) {
                                layer.alert("您不是管理员，没有权限进行该操作");
                            } else if (data.status == 501) {
                                layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                            }
                        }
                    });
                    layer.close(index);
                });
                break;
            }
        case 'batchDeletion':
            var data = checkStatus.data;
            var s = JSON.stringify(data);
            if(data.length<=0){
                alert("请选择需要更新的内容!");
                return;
            }
                layer.confirm('真的删除该字段吗', function (index) {
                    $.ajax({
                        url: '/manage/control-table/deleteBachViolation',
                        type: 'post',
                        dataType: 'json',
                        data:{data:s},
                        success: function (data) {
                            if (data.status == 200) {
                                layer.alert("删除成功！", function () {
                                    window.parent.location.reload();//刷新父页面
                                    parent.layer.close(index);//关闭弹出层
                                });
                            } else if (data.status == 500) {
                                layer.alert("您不是管理员，没有权限进行该操作");
                            } else if (data.status == 501) {
                                layer.alert("您打算删除的用户是其它管理员，您没有权限进行此操作");
                            }
                        }
                    });
                    layer.close(index);
                });
            case 'getDynicInfo':
                table.render({
                    elem: '#test'
                    , url: '/manage/user-dynamic/dynamicList'
                    , title: '用户动态表'
                    , toolbar: '#toolbarDynic'
                    , skin: 'row' //表格风格
                    , even: true
                    , page: true //是否显示分页
                    , limits: [5, 10, 20]
                    , limit: 10 //每页默认显示的数量
                    ,where: {
                        dynaminContent: $("#content").val(),
                        username: $("#micName").val()
                    }
                    , done: function (res) {
                        console.info("username:"+$("#micName").val())
                        console.info(res+"=="+username)
                        alert($("#content").val())
                        $("#content").val($("#content").val());
                        $("#content").val($("#content").val());
                        userPage.data = res.data;
                    }
                    , cols: [[
                        // {type: 'checkbox', fixed: 'left'}
                        , {field: 'id', title: 'ID'}
                        , {field: 'username', title: '用户名'}
                        , {field: 'dynaminContent', title: '动态内容'}
                        , {field: 'createTime', title: '发布时间'}
                        , {field: 'deleteStatusString', title: '是否删除'}
                        , {fixed: 'right', title: '操作', toolbar: '#dynamicBar', width: 500}
                    ]]
                    , page: true
                });
        }
    });
    var laydate = layui.laydate;
    //日期范围
    laydate.render({
        elem: '#test6',
        range: true
    });
})
