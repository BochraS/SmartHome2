export interface IConfValue {
    id?: number;
    value?: string;
    configId?: number;
}

export class ConfValue implements IConfValue {
    constructor(public id?: number, public value?: string, public configId?: number) {}
}
