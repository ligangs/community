function post() {
    var parent_id = $("#parent_id").val();
    var comment_content = $("#comment_content").val();
    $.ajax({
        url: "/comment",
        data: JSON.stringify({
            parentId: parent_id,
            comment: comment_content,
            type: 1
        }),
        type: "post",
        contentType: "application/json",
        success: function (data) {
            if (data.code == 200) {
                $("#comment_area").hide();
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
    })
}