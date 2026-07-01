import React, { useId } from "react";
import Input from "../../atoms/Input/Input";
import styles from "./FormField.module.css";

type BaseProps = {
  label: string;
  error?: string;
  multiline?: boolean;
};

export type FormFieldProps = BaseProps &
  Omit<React.InputHTMLAttributes<HTMLInputElement> & React.TextareaHTMLAttributes<HTMLTextAreaElement>, keyof BaseProps>;

export const FormField: React.FC<FormFieldProps> = ({ label, error, multiline, id, ...rest }) => {
  const autoId = useId();
  const fieldId = id ?? autoId;

  return (
    <div className={styles.field}>
      <label className={styles.label} htmlFor={fieldId}>
        {label}
      </label>
      {multiline ? (
        <Input multiline id={fieldId} aria-invalid={Boolean(error)} {...(rest as React.TextareaHTMLAttributes<HTMLTextAreaElement>)} />
      ) : (
        <Input id={fieldId} aria-invalid={Boolean(error)} {...(rest as React.InputHTMLAttributes<HTMLInputElement>)} />
      )}
      {error && <p className={styles.error}>{error}</p>}
    </div>
  );
};

export default FormField;
