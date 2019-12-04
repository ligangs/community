function post() {
    var parent_id = $("#parent_id").val();
    var comment_content = $("#comment_content").val();
    comment2target(parent_id, 1, comment_content);
}
function comment2target(targetId,type,content) {
    if (!content) {
        alert("评论不能为空哦");
        return ;
    }
    $.ajax({
        url: "/comment",
        data: JSON.stringify({
            parentId: targetId,
            comment: content,
            type: type
        }),
        type: "post",
        contentType: "application/json",
        success: function (data) {
            if (data.code == 200) {
                window.location.reload();
            } else {
                if (data.code == 2003) {
                    var conf = confirm(data.message);
                    if (conf) {
                        window.localStorage.setItem("isclose", "true");
                        window.open("https://github.com/login/oauth/authorize?client_id=2b28008b97c0ba4323e9&redirect=http://localhost:8080/callback&state=1&scope=user");
                    }
                } else {
                    alert(data.message);
                }
            }
        },
        dataType: "json",
    });
}

function comment(e) {
    var targetId=e.getAttribute("data-id");
    var content = $("#input-" + targetId).val();
    comment2target(targetId, 2, content);
}

function CommentCollapse(e) {
    var id=e.getAttribute("data-id");
    var comments_status = e.getAttribute("comments_status");
    var comment = $("#comment_" + id);
    //显示太为空，需要显示二级评论
    if (!comments_status) {
        if(comment.children().length==1) {
            //发送请求，得到服务器端返回数据，渲染到页面
            $.post( "/comment/"+id, function( data ) {
                $.each(data.data.reverse(),function(n,value){
                    var str = '<div class="media"> <div class="media-left"> <a href="#"> <img class="media-object img-rounded" src="'+ value.user.avatarUrl+'"> </a> ' +
                        '</div> <div class="media-body"> <span> '+value.user.name +'</span><br/> <span> '+ value.comment+'</span><br/> <span class="pull-right text-description"> '+ timeStamp2String(value.gmtCreate)+'</span> ' +
                        '<hr> </div> </div>';
                    comment.prepend(str);
                });
            }, "json" );
            comment.addClass("in");
            e.classList.add("active");
            e.setAttribute("comments_status", "in");
        }else{
            comment.addClass("in");
            e.classList.add("active");
            e.setAttribute("comments_status", "in");
        }
    }
    //关闭二级评论
    else {
        comment.removeClass("in");
        e.classList.remove("active");
        e.removeAttribute("comments_status");
    }
}

//在Jquery里格式化Date日期时间数据
function timeStamp2String(time){
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}