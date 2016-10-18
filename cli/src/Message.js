export class Message {
  static fromJSON (buffer) {
    return new Message(JSON.parse(buffer.toString()))
  }
// Added host and port here - and then removed them because they broke things
  constructor ({ host, port, username, command, contents }) {
    this.username = username
    this.command = command
    this.contents = contents
  }
// Added host & port to JSON - and then removed them because they broke things
  toJSON () {
    return JSON.stringify({
      username: this.username,
      command: this.command,
      contents: this.contents
    })
  }

  toString () {
    return this.contents
  }
}
