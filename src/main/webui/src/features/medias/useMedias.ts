import { useQuery } from "@tanstack/react-query";
import { fetchMedias } from "./mediasApi";
import { queryKeys } from "../queryKeys";

export function useMedias() {
  return useQuery({ queryKey: queryKeys.medias.all, queryFn: fetchMedias });
}
