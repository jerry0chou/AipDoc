
from flask import Flask, request, jsonify
app = Flask(__name__)

 

@app.route('/user/getUser', methods=['POST'])
def user_getUser():
    print(request.json)
    result={
  "username":"你好",
  "password":12345
}
    return jsonify(result)



@app.route('/user/deleteUser', methods=['POST'])
def user_deleteUser():
    print(request.json)
    result={
  "msg":"ok"
}
    return jsonify(result)



if __name__ == '__main__':
    app.run(debug=True)

