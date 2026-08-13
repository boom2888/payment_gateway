export function postMessage(type) {
    console.log("type",type)
    window.parent.postMessage(
      {
        type: type
      },
      "*"
    );
  }