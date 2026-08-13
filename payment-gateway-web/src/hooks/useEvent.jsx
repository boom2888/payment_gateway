import { useState, useCallback, useRef } from "react";

export const useDebouncedLoading = (delay = 300) => {
  const [isLoading, setIsLoading] = useState(false);
 // const loading = useRef(false)
  const loading = useRef(false);
  const timerRef = useRef(null);

  const debounceCallback = useCallback((fun) => {
    if (loading.current) return

    if (timerRef.current) {
      clearTimeout(timerRef.current);
    }

    return new Promise( ( resolve,reject) =>{
      timerRef.current = setTimeout(async()=>{
        try{
          loading.current = true
          setIsLoading(true)
           let res = await fun()
           resolve(res)
        }catch(err){
          reject(err)
        } finally{
          loading.current = false,
          setIsLoading(false)
        }
      },delay)
      
    })
  });
  return { isLoading, debounceCallback };
};
