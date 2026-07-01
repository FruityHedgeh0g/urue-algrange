import { useQuery } from "@tanstack/react-query";
import { fetchMedias } from "./mediasApi";

export function useMedias() {
  return useQuery({ queryKey: ["medias"], queryFn: fetchMedias });
}
