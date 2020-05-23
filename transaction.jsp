<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="../header.html" %>

<p>追加したい人を入力してください</p>
<form action="transaction" method="post">
  ID<input type="text" name="id">
  名前<input type="text" name="name">
  性別<input type="text" name="gender">
 <input type="submit" value="追加">
</form>

<p>※IDは最新の数字、名前は大文字アルファベット、性別は(M/F)で入力</p>

<%@include file="../footer.html" %>
