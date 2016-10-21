import vorpal from 'vorpal'
import { words } from 'lodash'
import { connect } from 'net'
import { Message } from './Message'

export const cli = vorpal()
// set host and port Commit 1.3
let host = host
let port = port
let username
let server

cli
  .delimiter(cli.chalk['yellow']('ftd~$'))
// Added <host> and <port> to .mode, as well as args for host and port. Commit 1.3
cli
  .mode('connect <host>, <port>, <username>')
  .delimiter(cli.chalk['green']('connected>'))
  .init(function (args, callback) {
    host = args.host
    port = args.port
    username = args.username
    server = connect({ host: 'localhost', port: 8080 }, () => {
      server.write(new Message({ username, command: 'connect' }).toJSON() + '\n')
      callback()
    })
// experimenting with adding the time stamp here.
    server.on('data', (buffer) => {
      this.log(Message.fromJSON(buffer).toString())
    })

    server.on('end', () => {
      cli.exec('exit')
    })
  })
  .action(function (input, callback) {
    const [ command, ...rest ] = words(input, /[^, ]+/g)
    const contents = rest.join(' ')
// added server.write commands for broadcast, @username and users. Commit 1.2
    if (command === 'disconnect') {
      server.end(new Message({ username, command, contents }).toJSON() + '\n')
    } else if (command === 'echo') {
      server.write(new Message({ username, command, contents }).toJSON() + '\n')
    } else if (command === 'broadcast') {
      server.write(new Message({ username, command, contents }).toJSON() + '\n')
    } else if (command.startsWith('@')) {
      server.write(new Message({ username, command, contents }).toJSON() + '\n')
    } else if (command === 'users') {
      server.write(new Message({ username, command, contents }).toJSON() + '\n')
    } else {
      this.log(`Command <${command}> was not recognized`)
    }

    callback()
  })
