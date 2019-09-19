### POST /users

Request:
```
{
  "username": "the username being registered",
  "password": "the password of the new user",
  "about": "about the new user"
}
```
201 Response:
```
{
  "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "username": "the user name"
  "about": "about the user"
}
```
400 Response:
```
"Username already in use."
```
---
### GET /users

200 Response:
```
[
  {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "username": "the user name"
    "about": "about the user"
  }
]
```
---
### POST /login

Request:
```
{
  "username": "the login username",
  "password": "the login password"
}
```
200 Response:
```
{
  "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "username": "the user name"
  "about": "about the user"
}
```
404 Response:
```
"Invalid credentials."
```
---
### POST /users/{userId}/timeline

Request:
```
{
  "text": "the post content"
}
```
201 Response:
```
"Post created."
```
400 Response:
```
"Post contains inappropriate language."
```
---
### GET /users/{userId}/timeline

200 Response:
```
[
  {
    "postId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "userId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "text": "the post content",
    "dateTime": "yyyy-MM-ddTHH:mm:ssZ"
  }
]
```
---
### POST /followings

Request:
```
{
  "followerId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
  "followeeId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```
201 Response:
```
"Following created."
```
400 Response:
```
"Following already exists."
```
---
### GET /followings/{followerId}/followees

200 Response:
```
[
  {
    "id": "the user id",
    "username": "the user name"
    "about": "about the user"
  }
]
```
404 Response:
```
"Follower not found."
```
---
### GET /users/{userId}/wall

200 Response:
```
[
  {
    "postId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "userId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
    "text": "the post content",
    "dateTime": "yyyy-MM-ddTHH:mm:ssZ"
  }
]
```