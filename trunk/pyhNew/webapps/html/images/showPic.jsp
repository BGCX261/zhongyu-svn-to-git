<%@page import="java.net.URL"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.io.OutputStream"%>
<%@page contentType="image/jpeg" language="java"%>



<%
	try {
        OutputStream os = response.getOutputStream();
        String picPath = request.getParameter("picurl");
        URLConnection u = new URL(picPath).openConnection();
        InputStream in = u.getInputStream();
        if (null != in) {
            int len;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) { 
                os.write(b, 0, len);
            }
            os.flush();
            in.close();
        }
        os.close();
        out.clear();
        out = pageContext.pushBody();
    }catch(Exception e){
        e.printStackTrace();
    }
%>
