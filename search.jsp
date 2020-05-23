<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="../header.html" %>

<p>登録会員のプロフィールを検索します</p>
<p>試しに「ANN」と検索してみてください</p>
<p>検索キーワードを入力してください</p>

<form action="search" method="post">
  <input type="text" name="keyword">
  <input type="submit" value="検索">
</form>

<%@include file="../footer.html" %>
