stanford-corenlp-server
=======================

Run [Stanford CoreNLP](http://nlp.stanford.edu/software/corenlp.shtml) as a JSON-RPC server.

## Using Docker

### Building Image

```bash
git submodule init
git submodule update
docker build -t akiomik/stanford-corenlp-server .
```

### Running Server

```bash
docker run -d -p 8081:8081 -p 9990:9990 akiomik/stanford-corenlp-server
```

### Querying the Server

```bash
curl -X POST http://localhost:8081/rpc -H "Content-type: application/json" -d '{"jsonrpc": "2.0", "method": "foo", "params": ["A martini. Shaken, not stirred."], "id": "1"}'
```

## Setup - Local

```bash
git clone https://github.com/akiomik/stanford-corenlp-server.git
cd stanford-corenlp-server

# build current version of Stanford CoreNLP
git submodule init
git submodule update
cd lib/CoreNLP
gradle build
```

## Usage - Local

### Server

```bash
sbt 'run -port=:8081'
```

### Client

```bash
curl -X POST http://localhost:8081/rpc -H "Content-type: application/json" -d '{"jsonrpc": "2.0", "method": "foo", "params": ["A martini. Shaken, not stirred."], "id": "1"}'

# => {"jsonrpc":"2.0","result":{"sentences":[{"index":"0","parse":"SENTENCE_SKIPPED_OR_UNPARSABLE\n","tokens":[{"index":"1","word":"A","characterOffsetBegin":"0","characterOffsetEnd":"1","pos":"DT"},{"index":"2","word":"martini","characterOffsetBegin":"2","characterOffsetEnd":"9","pos":"NN"},{"index":"3","word":".","characterOffsetBegin":"9","characterOffsetEnd":"10","pos":"."},{"index":"1","word":"Shaken","characterOffsetBegin":"11","characterOffsetEnd":"17","pos":"VBN"},{"index":"2","word":",","characterOffsetBegin":"17","characterOffsetEnd":"18","pos":","},{"index":"3","word":"not","characterOffsetBegin":"19","characterOffsetEnd":"22","pos":"RB"},{"index":"4","word":"stirred","characterOffsetBegin":"23","characterOffsetEnd":"30","pos":"VBN"},{"index":"5","word":".","characterOffsetBegin":"30","characterOffsetEnd":"31","pos":"."}]},{"index":"1","parse":"SENTENCE_SKIPPED_OR_UNPARSABLE\n","tokens":[{"index":"1","word":"A","characterOffsetBegin":"0","characterOffsetEnd":"1","pos":"DT"},{"index":"2","word":"martini","characterOffsetBegin":"2","characterOffsetEnd":"9","pos":"NN"},{"index":"3","word":".","characterOffsetBegin":"9","characterOffsetEnd":"10","pos":"."},{"index":"1","word":"Shaken","characterOffsetBegin":"11","characterOffsetEnd":"17","pos":"VBN"},{"index":"2","word":",","characterOffsetBegin":"17","characterOffsetEnd":"18","pos":","},{"index":"3","word":"not","characterOffsetBegin":"19","characterOffsetEnd":"22","pos":"RB"},{"index":"4","word":"stirred","characterOffsetBegin":"23","characterOffsetEnd":"30","pos":"VBN"},{"index":"5","word":".","characterOffsetBegin":"30","characterOffsetEnd":"31","pos":"."}]}]},"error":null,"id":"1"}
```

## Configuration

Edit `src/main/resources/application.properties`.
