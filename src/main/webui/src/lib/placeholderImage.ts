const PALETTE: [string, string][] = [
  ["#145d9e", "#0d3f6c"],
  ["#ea4a2e", "#b93a22"],
  ["#5aa9e6", "#145d9e"],
  ["#ff7a54", "#ea4a2e"],
];

function hashSeed(seed: string): number {
  let hash = 0;
  for (let i = 0; i < seed.length; i++) {
    hash = (hash << 5) - hash + seed.charCodeAt(i);
    hash |= 0;
  }
  return Math.abs(hash);
}

/**
 * Génère une image de substitution (dégradé de marque + libellé), en attendant
 * que le backend expose un endpoint de contenu pour les médias réels.
 */
export function placeholderImage(seed: string, label: string): string {
  const [from, to] = PALETTE[hashSeed(seed) % PALETTE.length];
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="640" height="400" viewBox="0 0 640 400">
      <defs>
        <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stop-color="${from}"/>
          <stop offset="100%" stop-color="${to}"/>
        </linearGradient>
      </defs>
      <rect width="640" height="400" fill="url(#g)"/>
      <text x="50%" y="50%" text-anchor="middle" dominant-baseline="middle"
            font-family="system-ui, sans-serif" font-size="28" font-weight="700" fill="#ffffff" opacity="0.9">
        ${label}
      </text>
    </svg>
  `.trim();
  return `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`;
}
