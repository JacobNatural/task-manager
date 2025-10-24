package com.app.taskmanager.configuration;

public class SwaggerExampleObjects {

    public static final String USER_RESPONSE_DTO = """
            {
                  "data": {
                    "id": "68f4101f04083690f4a2df13",
                    "firstName": "John",
                    "lastName": "Doe",
                    "username": "SkyForest"
                  },
                  "message": "success",
                  "timestamp": "2025-10-24T12:00:00Z"
                }
            """;

    public static final String USER_RESPONSE_LIST = """
            {
                                              "data": {
                                                "list": [
                                                  {
                                                    "id": "68f4101f04083690f4a2df13",
                                                    "name": "Jony",
                                                    "surname": "Deep",
                                                    "username": "SkyForest"
                                                  },
                                                  {
                                                    "id": "68f4101f04083690f4a2df14",
                                                    "name": "Merlin",
                                                    "surname": "Tony",
                                                    "username": "Tony667"
                                                  }
                                                ],
                                                "total": 10,
                                                "page": 0,
                                                "size": 2
                                              },
                                              "message": "success",
                                              "timestamp": "2025-10-23T23:58:20.405Z"
                                            }
            """;

    public static final String ID_RESPONSE_DTO = """
            {
                "data": {
                "id": "68f0d968af9623a741efba36"
               },
                "message": "success",
                "timestamp": "2025-10-23T23:59:39.527Z"
                }
            """;

    public static final String UPDATE_RESPONSE = """
            {
               "data": {
                 "matched": 1,
                 "modified": 1
               },
               "message": "success",
               "timestamp": "2025-10-24T00:03:48.347Z"
             }
            """;

    public static final String TASK_RESPONSE_DTO = """
            {
              "data": {
                "id": "68f4101f04083690f4a2df13",
                "firstName": "John",
                "lastName": "Doe",
                "username": "SkyForest"
              },
              "message": "success",
              "timestamp": "2025-10-24T12:00:00Z"
            }
            """;

    public static final String TASK_RESPONSE_LIST = """
            {
            "data": {
              "list": [
                {
                  "id": "68f245b1d494b40b89286165",
                  "title": "Finish documentation",
                  "description": "Write Swagger examples for all endpoints",
                  "creationDate": "2025-10-22T19:45:37.000Z",
                  "status": "TO_DO",
                  "userID": "68f4101f04083690f4a2df13"
                },
                {
                  "id": "68f245b1d494b40b89286167",
                  "title": "Finish the security",
                  "description": "Write filters for security",
                  "creationDate": "2025-09-24T18:45:37.000Z",
                  "status": "TO_DO",
                  "userID": "68f4101f04083690f4a2df13"
                }
              ],
              "total": 10,
              "page": 0,
              "size": 2
            },
             "message": "success",
             "timestamp": "2025-10-23T23:58:20.405Z"
            }
            """;

    public static final String TASK_NOT_FOUND = """
            {
                  "data": null,
                  "message": "Task not found",
                  "timestamp": "2025-10-24T12:00:00Z"
                }
            """;

    public static final String USER_NOT_FOUND = """
            {
                  "data": null,
                  "message": "User not found",
                  "timestamp": "2025-10-24T12:00:00Z"
                }
            """;

    public static final String BAD_REQUEST = """
            {
                  "data": null,
                  "message": "Invalid input format",
                  "timestamp": "2025-10-24T12:00:00Z"
                }
            """;

    public static final String INTERNAL_ERROR = """
            {
                  "data": null,
                  "message": "Internal server error",
                  "timestamp": "2025-10-24T12:00:00Z"
                }
            """;


}
