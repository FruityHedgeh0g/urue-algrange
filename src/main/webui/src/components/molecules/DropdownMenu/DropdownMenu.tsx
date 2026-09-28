import React, { useEffect, useRef, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import Icon from "../../atoms/Icon/Icon";
import styles from "./DropdownMenu.module.css";

export interface DropdownItem {
  label: string;
  /** Chemin interne (route ou ancre "/#section"), résolu via React Router. */
  to: string;
}

export interface DropdownMenuProps {
  label: string;
  items: DropdownItem[];
}

export const DropdownMenu: React.FC<DropdownMenuProps> = ({ label, items }) => {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);
  const { pathname } = useLocation();
  const isActive = items.some((item) => !item.to.includes("#") && pathname.startsWith(item.to));

  useEffect(() => {
    if (!open) return;
    const handleClick = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape") setOpen(false);
    };
    document.addEventListener("mousedown", handleClick);
    document.addEventListener("keydown", handleKeyDown);
    return () => {
      document.removeEventListener("mousedown", handleClick);
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [open]);

  return (
    <div
      className={styles.dropdown}
      ref={ref}
      onMouseEnter={() => setOpen(true)}
      onMouseLeave={() => setOpen(false)}
    >
      <button
        className={`${styles.trigger}${isActive ? ` ${styles.active}` : ""}`}
        aria-haspopup="menu"
        aria-expanded={open}
        onClick={() => setOpen((v) => !v)}
        type="button"
      >
        {label}
        <Icon name="chevronDown" size={14} strokeWidth={2.5} className={styles.caret} />
      </button>
      {open && (
        <ul className={styles.menu} role="menu">
          {items.map((item) => (
            <li key={item.to} role="none">
              <Link className={styles.item} role="menuitem" to={item.to} onClick={() => setOpen(false)}>
                {item.label}
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default DropdownMenu;
