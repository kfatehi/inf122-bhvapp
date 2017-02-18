const uuid = require('uuid');
const stream = require('fs').createWriteStream('timeseries.properties');

stream.write('ui=web\n');
stream.write('users.alice.type=Child\n');
stream.write('child.alice.modes=positive\n');

const ids = [];

const addToken = (date) => {
  const id = uuid.v4();
  stream.write(`token.alice.${id}.time=${date.getTime()}\n`);
  stream.write(`token.alice.${id}.note=automatic\n`);
  ids.push(id);
}

addToken(new Date('2017-02-1'))
addToken(new Date('2017-02-1'))
addToken(new Date('2017-02-1'))
addToken(new Date('2017-02-1'))
addToken(new Date('2017-02-1'))
addToken(new Date('2017-02-2'))
addToken(new Date('2017-02-2'))
addToken(new Date('2017-02-3'))
addToken(new Date('2017-02-4'))
addToken(new Date('2017-02-7'))
addToken(new Date('2017-02-7'))
addToken(new Date('2017-02-8'))
addToken(new Date('2017-02-9'))
addToken(new Date('2017-02-12'))
addToken(new Date('2017-02-12'))
addToken(new Date('2017-02-13'))
addToken(new Date('2017-02-14'))
addToken(new Date('2017-02-14'))
addToken(new Date('2017-02-18'))
addToken(new Date('2017-02-18'))
addToken(new Date('2017-02-18'))
addToken(new Date('2017-02-18'))
addToken(new Date('2017-02-21'))
addToken(new Date('2017-02-21'))
addToken(new Date('2017-02-22'))
addToken(new Date('2017-02-23'))
addToken(new Date('2017-02-24'))
addToken(new Date('2017-02-24'))
addToken(new Date('2017-02-25'))
addToken(new Date('2017-02-25'))
addToken(new Date('2017-02-25'))
addToken(new Date('2017-02-25'))
addToken(new Date('2017-02-26'))
addToken(new Date('2017-02-27'))
addToken(new Date('2017-02-27'))
addToken(new Date('2017-02-28'))

stream.write(`tokens.alice=${ids.join(',')}\n`);
