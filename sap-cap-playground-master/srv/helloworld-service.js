module.exports = (say)=>{
    say.on ('hello', req => `Hello ${req.data.to}!`)
  }
// es style
//   module.exports = class say {
//     hello(req) { return `Hello ${req.data.to}!` }
//   }

// http://localhost:4004/say/hello(to='world')