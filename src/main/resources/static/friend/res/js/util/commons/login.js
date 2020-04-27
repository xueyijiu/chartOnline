//  $('.loginForm').bootstrapValidator({
// 　　　　　　　　message: 'This value is not valid',
//             　feedbackIcons: {
//                 　　　　　　　　valid: 'glyphicon glyphicon-ok',
//                 　　　　　　　　invalid: 'glyphicon glyphicon-remove',
//                 　　　　　　　　validating: 'glyphicon glyphicon-refresh'
//             　　　　　　　　   },
//             fields: {
//
//             	phone: {
//                 message: '用户名验证失败',
//                 validators: {
//                     notEmpty: {
//                         message: '用户名不能为空'
//                     },
//
//
//                  }
//              },
//
//             password: {
//              		validators: {
//                      	notEmpty: {
//                         	message: '密码不能为空'
//                      	},
//
//                      stringLength: {
//                          min: 6,
//                          max: 30,
//                          message: '密码长度必须在6到12之间'
//                      }
//                  }
//             },
//
// }
//         });
        $('.loginSubmit').click(function() {
        	// $('.loginForm').bootstrapValidator('validate');
                var password = $(".password").val();
                var username = $(".username").val();
                $.ajax({
                    //几个参数需要注意一下
                    type: "POST",//方法类型
                    dataType: "json",//预期服务器返回的数据类型
                    url: "/userinfo/login" ,//url
                    data: {username:username,password:password},
                    success: function (result) {
                        console.log(result);//打印服务端返回的数据(调试用)
                        if (result.status == 200) {
                            window.location.href="/user-dynamic/allDynamicInfo?pageIndex=1&pageSize=5"
                        }else{
                            alert(result.message)
                        };
                    },
                    error : function() {
                        alert("异常！"+result);
                    }
                });
                return false;
        	});

  	 
