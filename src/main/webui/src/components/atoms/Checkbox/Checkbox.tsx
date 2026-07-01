import React, { useId } from "react";
import styles from "./Checkbox.module.css";

export interface CheckboxProps {
  label: string;
  checked: boolean;
  onChange: (checked: boolean) => void;
  disabled?: boolean;
}

export const Checkbox: React.FC<CheckboxProps> = ({ label, checked, onChange, disabled = false }) => {
  const id = useId();
  return (
    <label className={styles.checkbox} htmlFor={id}>
      <input
        id={id}
        type="checkbox"
        checked={checked}
        disabled={disabled}
        onChange={(e) => onChange(e.target.checked)}
        className={styles.input}
      />
      <span>{label}</span>
    </label>
  );
};

export default Checkbox;
