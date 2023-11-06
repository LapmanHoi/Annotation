package datalake.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datalake.data.DBConn;
import datalake.manager.DLManager;
import datalake.model.DLBean;
import datalake.utility.DLTool;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: RESTful Web Service
 */
@Path("/")
public class DLService {
  private static final String PATH = "/info";
  private static final String MESG = "Request from: %s (%s)";

  /**
   * Generated web service with POST method for CREATE transaction
   * 
   * @param bean     hold the data record for insert to database
   * @param request  HTTP request object (input stream)
   * @param response HTTP response object (output stream)
   * @return result messages
   * @throws Exception
   */
  @POST
  @Path(PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createInfo(DLBean bean, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
    DLTool.writeLog(this.getClass()).info(String.format(MESG, request.getRemoteAddr().toString(), PATH));
    DBConn dbObject = new DBConn();
    DLManager manager = (DLManager) Class.forName("Manager").getDeclaredConstructor().newInstance();
    int rc = manager.create(dbObject, bean);
    dbObject.closeConn();
    return Response.ok(rc).build();
  }

  /**
   * Generated web service with GET method for READ transaction
   * 
   * @param bean     hold the data record for insert to database
   * @param request  HTTP request object (input stream)
   * @param response HTTP response object (output stream)
   * @return Java bean to hold the resulting data record
   * @throws Exception
   */
  @GET
  @Path(PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public DLBean readInfo(DLBean bean, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
    DLTool.writeLog(this.getClass()).info(String.format(MESG, request.getRemoteAddr().toString(), PATH));
    DBConn dbObject = new DBConn();
    DLManager manager = (DLManager) Class.forName("Manager").getDeclaredConstructor().newInstance();
    bean = manager.read(dbObject, bean);
    dbObject.closeConn();
    return bean;
  }

  /**
   * Generated web service with PUT method for UPDATE transaction
   * 
   * @param bean     hold the data record for insert to database
   * @param request  HTTP request object (input stream)
   * @param response HTTP response object (output stream)
   * @return result messages
   * @throws Exception
   */
  @PUT
  @Path(PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateInfo(DLBean bean, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
    DLTool.writeLog(this.getClass()).info(String.format(MESG, request.getRemoteAddr().toString(), PATH));
    DBConn dbObject = new DBConn();
    DLManager manager = (DLManager) Class.forName("Manager").getDeclaredConstructor().newInstance();
    int rc = manager.update(dbObject, bean);
    dbObject.closeConn();
    return Response.ok(rc).build();
  }

  /**
   * Generated web service with DELETE method for DELETE transaction
   * 
   * @param bean     hold the data record for insert to database
   * @param request  HTTP request object (input stream)
   * @param response HTTP response object (output stream)
   * @return result messages
   * @throws Exception
   */
  @DELETE
  @Path(PATH)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteInfo(DLBean bean, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
    DLTool.writeLog(this.getClass()).info(String.format(MESG, request.getRemoteAddr().toString(), PATH));
    DBConn dbObject = new DBConn();
    DLManager manager = (DLManager) Class.forName("Manager").getDeclaredConstructor().newInstance();
    int rc = manager.delete(dbObject, bean);
    dbObject.closeConn();
    return Response.ok(rc).build();
  }
}