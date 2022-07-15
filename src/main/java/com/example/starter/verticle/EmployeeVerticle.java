package com.example.starter.verticle;

import java.util.HashMap;
import com.example.starter.model.Employee;
import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class EmployeeVerticle extends AbstractVerticle{
    private final String jsonApp = "application/json;charset=UTF-8";
    private final String contentType = "content-type";

    private HashMap<Integer, Employee> employees = new HashMap<>();
    public void createData() {
        employees.put(1, new Employee("1", "Duong"));
        employees.put(2, new Employee("2", "Hieu"));
        employees.put(3, new Employee("3", "Dat"));
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
       createData();
       HttpServer server = vertx.createHttpServer();
       Router router = Router.router(vertx);
       router.route().handler(BodyHandler.create());
       server.requestHandler(router).listen(8080);
        // Show All Employees
       router.get("/employees").handler(this::showAllEmployees);

       // Get one Employee
       router.get("/employees/:id").handler(this::getOneEmployee);

       //Add employee
       router.post("/employees").handler(this::addEmployees);

       //Edit employee
       router.put("/employees").handler(this::editEmployee);

       //Edit employee
       router.delete("/employees/:id").handler(this::deleteEmployee);

    }
    private void showAllEmployees (RoutingContext routingContext) {
        Gson gson = new Gson();
        String json = gson.toJson(employees);
        HttpServerResponse response = routingContext.response();
        response.putHeader(contentType, jsonApp);
        response.end(json);
    }

    private void getOneEmployee (RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader(contentType, jsonApp);
        String strId = routingContext.request().getParam("id");
        if (strId == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            int id = Integer.parseInt(strId);
            Employee empFound = employees.get(id);
            Gson gson = new Gson();
            String json = gson.toJson(empFound);
            response.end(json);
        }
    }

    private void addEmployees (RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader(contentType, jsonApp);
        try {
            String strJson = routingContext.body().asJsonObject().encode();
            Gson gson = new Gson();
            Employee emp = gson.fromJson(strJson, Employee.class);
            employees.put(Integer.parseInt(emp.getId()), emp);
            response.end("OK");
        } catch (Exception e){
            response.end(e.getMessage());
        }
    }

    private void editEmployee (RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader(contentType, jsonApp);
        String strJon = routingContext.body().asJsonObject().encode();
        Gson gson = new Gson();
        Employee emp = gson.fromJson(strJon, Employee.class);
        if (employees.containsKey(Integer.parseInt(emp.getId()))) {
            employees.put(Integer.parseInt(emp.getId()), emp);
            response.end("OK");
        } else {
            response.end("Fail");
        }
    }

    private void deleteEmployee (RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader(contentType, jsonApp);
        String strId = routingContext.request().getParam("id");
        int id = Integer.parseInt(strId);
        if (employees.containsKey(id)) {
            employees.remove(id);
            response.end("true");
        } else {
            response.end("fales");
        }
    }
}
