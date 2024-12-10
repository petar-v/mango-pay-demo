### Front-End Integration Specification

#### Base URL
All API requests are prefixed by:
```
https://<backend-base-url>/ideas
```

---

### API Endpoints

#### **1. Get All Ideas**
**Endpoint**: `GET /ideas`  
**Description**: Fetches a list of all project ideas.  
**Authorization**: Not required.

**Sample Request**:
```http
GET /ideas HTTP/1.1
Host: <backend-base-url>
```

**Sample Response**:
```json
[
  {
    "id": 1,
    "name": "Cool Idea",
    "description": "A description of the cool idea",
    "commentCount": 2,
    "likeCount": 5,
    "author": {
      "id": 101,
      "name": "Jane Doe",
      "email": "jane.doe@example.com"
    }
  }
]
```

---

#### **2. Get Idea Details**
**Endpoint**: `GET /ideas/{id}`  
**Description**: Fetches detailed information about a specific project idea, including comments and likes.  
**Authorization**: Optional. If authenticated, includes `isLikedByCurrentUser`.

**Sample Request**:
```http
GET /ideas/1 HTTP/1.1
Host: <backend-base-url>
Authorization: Bearer <jwt-token>
```

**Sample Response**:
```json
{
  "id": 1,
  "name": "Cool Idea",
  "description": "A detailed description of the cool idea.",
  "imageUrl": "https://example.com/image.jpg",
  "author": {
    "id": 101,
    "name": "Jane Doe",
    "email": "jane.doe@example.com"
  },
  "likeCount": 5,
  "comments": [
    {
      "id": 201,
      "text": "This is a great idea!",
      "author": {
        "id": 102,
        "name": "John Smith",
        "email": "john.smith@example.com"
      }
    }
  ],
  "isLikedByCurrentUser": true
}
```

---

#### **3. Create a New Idea**
**Endpoint**: `POST /ideas`  
**Description**: Allows logged-in users to create a new project idea.  
**Authorization**: Required.
**Output**: Returns the ID of the new project idea.  

**Request Body**:
```json
{
  "name": "My New Idea",
  "description": "This is a description of my new idea."
}
```

**Sample Request**:
```http
POST /ideas HTTP/1.1
Host: <backend-base-url>
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "My New Idea",
  "description": "This is a description of my new idea."
}
```

**Response**:
```json
10
```

---

#### **4. Add a Comment**
**Endpoint**: `PUT /ideas/{id}/comments`  
**Description**: Adds a comment to a specific idea.  
**Authorization**: Required.

**Request Body**:
```json
{
  "text": "This is my comment on this idea."
}
```

**Sample Request**:
```http
PUT /ideas/1/comments HTTP/1.1
Host: <backend-base-url>
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "text": "This is my comment on this idea."
}
```

**Response**:
```json
"Comment added"
```

---

#### **5. Like an Idea**
**Endpoint**: `POST /ideas/{id}/like`  
**Description**: Likes a specific idea.  
**Authorization**: Required.

**Sample Request**:
```http
POST /ideas/1/like HTTP/1.1
Host: <backend-base-url>
Authorization: Bearer <jwt-token>
```

**Response**:
```json
"Article is liked"
```

---

#### **6. Dislike an Idea**
**Endpoint**: `POST /ideas/{id}/dislike`  
**Description**: Removes a like from a specific idea.  
**Authorization**: Required.

**Sample Request**:
```http
POST /ideas/1/dislike HTTP/1.1
Host: <backend-base-url>
Authorization: Bearer <jwt-token>
```

**Response**:
```json
"Article is disliked"
```

---

#### **7. Delete an Idea**
**Endpoint**: `DELETE /ideas/{id}`  
**Description**: Deletes a specific idea if the user is its author.  
**Authorization**: Required.

**Sample Request**:
```http
DELETE /ideas/1 HTTP/1.1
Host: <backend-base-url>
Authorization: Bearer <jwt-token>
```

**Response**:
```json
"Idea deleted successfully"
```

---

### Authentication
To access secured endpoints, include the `Authorization` header with a JWT token:

```http
Authorization: Bearer <jwt-token>
```

If the token is invalid or missing, the server will respond with a `401 Unauthorized` error.

---
