$('.formVild').bootstrapValidator({
　　　　　　　　message: 'This value is not valid',
            　feedbackIcons: {
                　　　　　　　　valid: 'glyphicon glyphicon-ok',
                　　　　　　　　invalid: 'glyphicon glyphicon-remove',
                　　　　　　　　validating: 'glyphicon glyphicon-refresh'
            　　　　　　　　   },
            fields: {

            	username: {
                message: '用户名验证失败',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                
               
                 }
             },
             phoneNumber:{
            			validators: {
  			notEmpty: {
                            message: '手机号不能为空'
                        },
                        regexp: {
                            regexp: /^1\d{10}$/,
                            message: '手机号格式错误'
                        }
  			}
  		},

            password: {
             		validators: {
                     	notEmpty: {
                        	message: '密码不能为空'
                     	},
                    identical: {
                        field: 'passwordAgain',
                        message: '两次密码不一致'
                    },
                   
                     	different: {
                        field: 'username',
                        message: '密码不能与用户名一致'
                     },
                     stringLength: {
                         min: 6,
                         max: 30,
                         message: '密码长度必须在6到12之间'
                     }
                 }
            },
            passwordAgain: {
            	validators: {
            	notEmpty: {
                        	message: '密码不能为空'
                     	},
                identical: {
                        field: 'password',
                        message: '两次密码不一致'
                    },
            }
         },
         	petName: {
         		validators: {
            		notEmpty: {
                        	message: '昵称不能为空'
                     	},
                
            }

         },
         sex: {
         	validators: {
            		notEmpty: {
                        	message: '性别不能为空'
                     	},
                
            }

         },
         userPic:{
         	validators: {
            		notEmpty: {
                        	message: '头像不能为空'
                     	},
                
            }
         }
     }

        });
        $('.submit').click(function() {
        	$('.formVild').bootstrapValidator('validate');
            var fd = new FormData(document.getElementById("formVild"));
            $.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "/userinfo/register",//url
                processData: false, //告诉jQuery不要去处理发送的数据
                contentType: false, //告诉jQuery不要去设置Content-Type请求头
                data: fd,
                success: function (result) {
                    console.log(result);//打印服务端返回的数据(调试用)
                    if (result.status == 200) {
                        window.location.href = "/"
                    } else {
                        alert(result.message)
                    };
                },
                error: function () {
                    alert("异常！" + result);
                }
            });
            return false;
        	});

  	 
