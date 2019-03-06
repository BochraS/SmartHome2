import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConfValue } from 'app/shared/model/conf-value.model';
import { AccountService } from 'app/core';
import { ConfValueService } from './conf-value.service';

@Component({
    selector: 'jhi-conf-value',
    templateUrl: './conf-value.component.html'
})
export class ConfValueComponent implements OnInit, OnDestroy {
    confValues: IConfValue[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected confValueService: ConfValueService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.confValueService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IConfValue[]>) => (this.confValues = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.confValueService.query().subscribe(
            (res: HttpResponse<IConfValue[]>) => {
                this.confValues = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInConfValues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IConfValue) {
        return item.id;
    }

    registerChangeInConfValues() {
        this.eventSubscriber = this.eventManager.subscribe('confValueListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
