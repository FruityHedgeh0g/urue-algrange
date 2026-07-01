const base = import.meta.env.BASE_URL.replace(/\/$/, "");

/** Résout un chemin de fichier public (dossier `public/`) sous le base path de service (`/quinoa`). */
export function assetUrl(path: string): string {
  return `${base}/${path.replace(/^\//, "")}`;
}
